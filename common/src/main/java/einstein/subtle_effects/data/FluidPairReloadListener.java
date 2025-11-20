package einstein.subtle_effects.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.data.splash_types.SplashTypeReloadListener;
import einstein.subtle_effects.util.FluidAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluidPairReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, FluidPair.Data>> implements NamedReloadListener {

    private static final String DIRECTORY = "subtle_effects/fluid_pairs";
    public static final Map<ResourceLocation, FluidPair> FLUID_PAIRS = new HashMap<>();

    @Override
    protected Map<ResourceLocation, FluidPair.Data> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, JsonElement> resources = new HashMap<>();
        SimpleJsonResourceReloadListener.scanDirectory(resourceManager, DIRECTORY, Util.GSON, resources);
        List<Fluid> sourceFluids = new ArrayList<>();
        List<Fluid> flowingFluids = new ArrayList<>();
        List<AbstractCauldronBlock> cauldrons = new ArrayList<>();
        Map<ResourceLocation, FluidPair.Data> fluidPairs = new HashMap<>();

        resources.forEach((id, element) ->
                FluidPair.Data.CODEC.parse(JsonOps.INSTANCE, element)
                        .resultOrPartial(error -> SubtleEffects.LOGGER.error("Failed to decode fluid pair with ID {} - Error: {}", id, error))
                        .ifPresent(pairData -> validate(id, pairData, sourceFluids, flowingFluids, cauldrons, fluidPairs))
        );
        return fluidPairs;
    }

    @Override
    protected void apply(Map<ResourceLocation, FluidPair.Data> resources, ResourceManager manager, ProfilerFiller profiler) {
        FLUID_PAIRS.forEach((id, fluidPair) -> {
            ((FluidAccessor) fluidPair.source()).subtleEffects$setFluidPair(null);
            ((FluidAccessor) fluidPair.flowing()).subtleEffects$setFluidPair(null);
            fluidPair.cauldron().ifPresent(cauldron -> ((FluidAccessor) cauldron).subtleEffects$setFluidPair(fluidPair));
        });

        FLUID_PAIRS.clear();

        resources.forEach((id, pairData) -> {
            Fluid source = pairData.source();
            Fluid flowing = pairData.flowing();
            FluidPair fluidPair = new FluidPair(id, source, flowing, pairData.cauldron(), pairData.splashTypeId().map(SplashTypeReloadListener.SPLASH_TYPES_BY_ID::get));
            ((FluidAccessor) source).subtleEffects$setFluidPair(fluidPair);
            ((FluidAccessor) flowing).subtleEffects$setFluidPair(fluidPair);
            pairData.cauldron().ifPresent(cauldron -> ((FluidAccessor) cauldron).subtleEffects$setFluidPair(fluidPair));
            FLUID_PAIRS.put(id, fluidPair);
        });
    }

    private static void validate(ResourceLocation id, FluidPair.Data pairData, List<Fluid> sourceFluids, List<Fluid> flowingFluids, List<AbstractCauldronBlock> cauldrons, Map<ResourceLocation, FluidPair.Data> fluidPairs) {
        Fluid source = pairData.source();
        Fluid flowing = pairData.flowing();

        if (source.isSame(Fluids.EMPTY) || flowing.isSame(Fluids.EMPTY)) {
            SubtleEffects.LOGGER.error("Fluid pair '{}' has empty fluid(s)", id);
            return;
        }

        if (sourceFluids.contains(source)) {
            SubtleEffects.LOGGER.error("Found duplicate source fluid in fluid pair: '{}'", id);
            return;
        }
        else if (flowingFluids.contains(flowing)) {
            SubtleEffects.LOGGER.error("Found duplicate flowing fluid in fluid pair: '{}'", id);
            return;
        }
        else if (pairData.cauldron().filter(cauldrons::contains).isPresent()) {
            SubtleEffects.LOGGER.error("Found duplicate cauldron in fluid pair: '{}'", id);
        }

        sourceFluids.add(source);
        flowingFluids.add(flowing);
        pairData.cauldron().ifPresent(cauldrons::add);
        fluidPairs.put(id, pairData);
    }

    @Override
    public ResourceLocation getId() {
        return SubtleEffects.loc("fluid_pairs");
    }
}
