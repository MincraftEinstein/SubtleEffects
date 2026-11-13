package einstein.subtle_effects.data.splash_types;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.data.DynamicSpriteSetsManager;
import einstein.subtle_effects.data.NamedReloadListener;
import einstein.subtle_effects.data.SpriteSetHolder;
import einstein.subtle_effects.data.color_providers.NoneColorProvider;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static einstein.subtle_effects.SubtleEffects.LOGGER;
import static einstein.subtle_effects.init.ModSpriteSets.*;

public class SplashTypeReloadListener extends SimplePreparableReloadListener<Map<Identifier, SplashType>> implements NamedReloadListener {

    private static final FileToIdConverter DIRECTORY = FileToIdConverter.json("subtle_effects/splash_types");
    public static final Map<Identifier, SplashType> SPLASH_TYPES = new HashMap<>();
    public static final Identifier ID = SubtleEffects.loc("splash_types");
    public static boolean HAS_PREPARED = false;

    // TODO possibly is sometimes loaded after dynamic sprite sets have begun loading
    @Override
    protected Map<Identifier, SplashType> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<Identifier, SplashType.Data> splashTypes = new HashMap<>();
        Map<Identifier, SplashType> validSplashTypes = new HashMap<>();

        scanDirectory(resourceManager, DIRECTORY, JsonOps.INSTANCE, SplashType.Data.CODEC, splashTypes);
        splashTypes.forEach((id, typeData) -> load(id, typeData, validSplashTypes));
        LOGGER.info("Loaded {} splash types", splashTypes.size());
        HAS_PREPARED = true;
        return validSplashTypes;
    }

    @Override
    protected void apply(Map<Identifier, SplashType> resources, ResourceManager manager, ProfilerFiller profiler) {
        HAS_PREPARED = false;
        SPLASH_TYPES.clear();
        SPLASH_TYPES.putAll(resources);
    }

    private static void load(Identifier id, SplashType.Data typeData, Map<Identifier, SplashType> splashTypes) {
        SplashOptions.Data overlayOptions = Either.unwrap(typeData.splashOverlayOptions().mapLeft(hasOverlay -> hasOverlay ? SplashOptions.Data.DEFAULT : SplashOptions.Data.EMPTY));

        splashTypes.put(id, new SplashType(createOptions(typeData.splashOptions(), WATER_SPLASH),
                createOptions(overlayOptions, overlayOptions.colorProvider().isPresent() ? WATER_SPLASH_OVERLAY : null),
                createOptions(typeData.splashRippleOptions(), WATER_SPLASH_RIPPLE),
                typeData.dropletOptions()
        ));
    }

    private static SplashOptions createOptions(SplashOptions.Data optionsData, @Nullable SpriteSetHolder defaultSprites) {
        // noinspection ConstantConditions
        return new SplashOptions(optionsData.spriteSetId().map(DynamicSpriteSetsManager::getOrCreate).orElse(defaultSprites),
                optionsData.colorProvider().orElse(NoneColorProvider.INSTANCE), optionsData.tinting(), optionsData.transparency()
        );
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    public static <T> void scanDirectory(
            final ResourceManager manager, final FileToIdConverter lister, final DynamicOps<JsonElement> ops, final Codec<T> codec, final Map<Identifier, T> result
    ) {
        for (Map.Entry<Identifier, Resource> entry : lister.listMatchingResources(manager).entrySet()) {
            Identifier location = entry.getKey();
            Identifier id = lister.fileToId(location);

            try {
                Reader reader = entry.getValue().openAsReader();

                try {
                    codec.parse(ops, StrictJsonParser.parse(reader)).ifSuccess(parsed -> {
                        if (result.putIfAbsent(id, parsed) != null) {
                            throw new IllegalStateException("Duplicate data file ignored with ID " + id);
                        }
                    }).ifError(error -> SubtleEffects.LOGGER.error("Couldn't parse data file '{}' from '{}': {}", id, location, error));
                } catch (Throwable var13) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var12) {
                            var13.addSuppressed(var12);
                        }
                    }

                    throw var13;
                }

                if (reader != null) {
                    reader.close();
                }
            } catch (IllegalArgumentException | IOException | JsonParseException var14) {
                SubtleEffects.LOGGER.error("Couldn't parse data file '{}' from '{}'", id, location, var14);
            }
        }
    }

}
