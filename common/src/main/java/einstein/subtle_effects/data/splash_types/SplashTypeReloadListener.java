package einstein.subtle_effects.data.splash_types;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.data.DynamicSpriteSetsManager;
import einstein.subtle_effects.data.NamedReloadListener;
import einstein.subtle_effects.data.SpriteSetHolder;
import einstein.subtle_effects.data.color_providers.NoneColorProvider;
import einstein.subtle_effects.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static einstein.subtle_effects.SubtleEffects.LOGGER;
import static einstein.subtle_effects.init.ModSpriteSets.*;

public class SplashTypeReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, SplashType>> implements NamedReloadListener {

    private static final String DIRECTORY = "subtle_effects/splash_types";
    public static final Map<ResourceLocation, SplashType> SPLASH_TYPES = new HashMap<>();

    @Override
    protected Map<ResourceLocation, SplashType> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, JsonElement> resources = new HashMap<>();
        Map<ResourceLocation, SplashType> splashTypes = new HashMap<>();
        SimpleJsonResourceReloadListener.scanDirectory(resourceManager, DIRECTORY, Util.GSON, resources);

        resources.forEach((id, element) ->
                SplashType.Data.CODEC.parse(JsonOps.INSTANCE, element)
                        .resultOrPartial(error -> LOGGER.error("Failed to decode splash type with ID {} - Error: {}", id, error))
                        .ifPresent(typeData -> load(id, typeData, splashTypes))
        );
        return splashTypes;
    }

    @Override
    protected void apply(Map<ResourceLocation, SplashType> resources, ResourceManager manager, ProfilerFiller profiler) {
        SPLASH_TYPES.clear();
        SPLASH_TYPES.putAll(resources);
    }

    private static void load(ResourceLocation id, SplashType.Data typeData, Map<ResourceLocation, SplashType> splashTypes) {
        SplashOptions.Data overlayOptions = typeData.splashOverlayOptions().mapLeft(hasOverlay -> hasOverlay ? SplashOptions.Data.DEFAULT : SplashOptions.Data.EMPTY).map(Function.identity(), Function.identity());

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
    public ResourceLocation getId() {
        return SubtleEffects.loc("splash_types");
    }
}
