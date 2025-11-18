package einstein.subtle_effects.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.FluidAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluidPairReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, FluidPair>> implements NamedReloadListener {

    private static final String DIRECTORY = "subtle_effects/fluid_pairs";
    public static final Map<ResourceLocation, FluidPair> FLUID_PAIRS = new HashMap<>();
    public static final Map<ResourceLocation, FluidPair> PREPARED_FLUID_PAIRS = new HashMap<>();

    @Override
    protected Map<ResourceLocation, FluidPair> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, JsonElement> resources = new HashMap<>();
        SimpleJsonResourceReloadListener.scanDirectory(resourceManager, DIRECTORY, Util.GSON, resources);
        List<Fluid> sourceFluids = new ArrayList<>();
        List<Fluid> flowingFluids = new ArrayList<>();
        Map<ResourceLocation, FluidPair> fluidPairs = new HashMap<>();

        resources.forEach((id, element) ->
                FluidPair.CODEC.parse(JsonOps.INSTANCE, element)
                        .resultOrPartial(error -> SubtleEffects.LOGGER.error("Failed to decode fluid pair with ID {} - Error: {}", id, error))
                        .ifPresent(fluidPair -> validate(id, fluidPair, sourceFluids, flowingFluids, fluidPairs))
        );
        return fluidPairs;
    }

    @Override
    protected void apply(Map<ResourceLocation, FluidPair> resources, ResourceManager manager, ProfilerFiller profiler) {
        FLUID_PAIRS.forEach((id, fluidPair) -> {
            ((FluidAccessor) fluidPair.source()).subtleEffects$setFluidPair(null);
            ((FluidAccessor) fluidPair.flowing()).subtleEffects$setFluidPair(null);
        });

        PREPARED_FLUID_PAIRS.clear();
        FLUID_PAIRS.clear();

        resources.forEach((id, fluidPair) -> {
            ((FluidAccessor) fluidPair.source()).subtleEffects$setFluidPair(fluidPair);
            ((FluidAccessor) fluidPair.flowing()).subtleEffects$setFluidPair(fluidPair);
            FLUID_PAIRS.put(id, fluidPair);
        });
    }

    private static void validate(ResourceLocation id, FluidPair fluidPair, List<Fluid> sourceFluids, List<Fluid> flowingFluids, Map<ResourceLocation, FluidPair> fluidPairs) {
        Fluid source = fluidPair.source();
        Fluid flowing = fluidPair.flowing();

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

        sourceFluids.add(source);
        flowingFluids.add(flowing);
        fluidPairs.put(id, fluidPair);
        PREPARED_FLUID_PAIRS.put(id, fluidPair);
    }

    @Override
    public ResourceLocation getId() {
        return SubtleEffects.loc("fluid_pairs");
    }
}
