package einstein.subtle_effects.data.splash_types;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.data.FluidPair;
import einstein.subtle_effects.data.FluidPairReloadListener;
import einstein.subtle_effects.data.NamedReloadListener;
import einstein.subtle_effects.data.color_providers.NoneColorProvider;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static einstein.subtle_effects.SubtleEffects.LOGGER;
import static einstein.subtle_effects.init.ModSpriteSets.*;

public class SplashTypeReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, SplashTypeData.SplashType>> implements NamedReloadListener {

    private static final String DIRECTORY = "subtle_effects/splash_types";
    public static final Map<FluidPair, ResourceLocation> FLUID_PAIR_TO_ID = new HashMap<>();
    public static final Map<ResourceLocation, SplashTypeData.SplashType> SPLASH_TYPES_BY_ID = new HashMap<>();
    public static final List<ResourceLocation> OLD_SPRITE_SETS = new ArrayList<>();
    public static final Map<ResourceLocation, ParticleEngine.MutableSpriteSet> SPRITE_SETS = new HashMap<>();

    @Override
    protected Map<ResourceLocation, SplashTypeData.SplashType> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, JsonElement> resources = new HashMap<>();
        SimpleJsonResourceReloadListener.scanDirectory(resourceManager, DIRECTORY, Util.GSON, resources);
        List<ResourceLocation> fluidPairs = new ArrayList<>();
        Map<ResourceLocation, SplashTypeData.SplashType> splashTypes = new HashMap<>();
        OLD_SPRITE_SETS.clear();
        OLD_SPRITE_SETS.addAll(SPRITE_SETS.keySet());
        SPRITE_SETS.clear();

        resources.forEach((id, element) ->
                SplashTypeData.CODEC.parse(JsonOps.INSTANCE, element)
                        .resultOrPartial(error -> LOGGER.error("Failed to decode splash type with ID {} - Error: {}", id, error))
                        .ifPresent(typeData -> load(id, typeData, fluidPairs, splashTypes))
        );
        return splashTypes;
    }

    @Override
    protected void apply(Map<ResourceLocation, SplashTypeData.SplashType> resources, ResourceManager manager, ProfilerFiller profiler) {
        FLUID_PAIR_TO_ID.clear();
        SPLASH_TYPES_BY_ID.clear();

        resources.forEach((id, type) -> {
            FLUID_PAIR_TO_ID.put(type.fluidPair(), id);
            SPLASH_TYPES_BY_ID.put(id, type);
        });
    }

    private static void load(ResourceLocation id, SplashTypeData typeData, List<ResourceLocation> fluidPairs, Map<ResourceLocation, SplashTypeData.SplashType> splashTypes) {
        ResourceLocation fluidPairId = typeData.fluidPair();
        if (fluidPairs.contains(fluidPairId)) {
            LOGGER.error("Found duplicate splash type '{}' with fluid pair '{}'", id, fluidPairId);
            return;
        }

        FluidPair fluidPair = FluidPairReloadListener.PREPARED_FLUID_PAIRS.get(fluidPairId);
        if (fluidPair == null) {
            LOGGER.error("Fluid pair '{}' referenced by splash type '{}' does not exist", fluidPairId, id);
            return;
        }

        SplashOptionsData overlayOptions = Either.unwrap(typeData.splashOverlayOptions().mapLeft(hasOverlay -> hasOverlay ? SplashOptionsData.DEFAULT : SplashOptionsData.EMPTY));
        SplashOptionsData dropletOptions = typeData.dropletOptions().orElse(SplashOptionsData.DEFAULT);

        fluidPairs.add(fluidPairId);
        splashTypes.put(id, new SplashTypeData.SplashType(fluidPair, typeData.lightEmission(),
                createOptions(typeData.splashOptions(), WATER_SPLASH),
                createOptions(overlayOptions, overlayOptions.colorProvider().isPresent() ? WATER_SPLASH_OVERLAY : null),
                createOptions(typeData.splashRippleOptions(), WATER_SPLASH_RIPPLE),
                new SplashOptionsData.SplashOptions(null, dropletOptions.colorProvider().orElse(NoneColorProvider.INSTANCE), dropletOptions.tinting(), dropletOptions.transparency())
        ));
    }

    private static SplashOptionsData.SplashOptions createOptions(SplashOptionsData optionsData, ParticleEngine.MutableSpriteSet defaultSprites) {
        final ParticleEngine.MutableSpriteSet[] sprites = new ParticleEngine.MutableSpriteSet[1];
        optionsData.spriteSetId().ifPresentOrElse(
                location -> {
                    if (SPRITE_SETS.containsKey(location)) {
                        sprites[0] = SPRITE_SETS.get(location);
                        return;
                    }

                    sprites[0] = new ParticleEngine.MutableSpriteSet();
                    SPRITE_SETS.put(location, sprites[0]);
                },
                () -> sprites[0] = defaultSprites
        );

        return new SplashOptionsData.SplashOptions(sprites[0], optionsData.colorProvider().orElse(NoneColorProvider.INSTANCE), optionsData.tinting(), optionsData.transparency());
    }

    @Override
    public ResourceLocation getId() {
        return SubtleEffects.loc("splash_types");
    }
}
