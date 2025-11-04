package einstein.subtle_effects.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.FluidAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluidPairReloadListener extends SimpleJsonResourceReloadListener implements NamedReloadListener {

    private static final String DIRECTORY = "subtle_effects/fluid_pairs";
    public static final Map<ResourceLocation, FluidPair> FLUID_PAIRS = new HashMap<>();

    public FluidPairReloadListener() {
        super(Util.GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller profiler) {
        FLUID_PAIRS.forEach((id, fluidPair) -> {
            ((FluidAccessor) fluidPair.source()).subtleEffects$setFluidPair(null);
            ((FluidAccessor) fluidPair.flowing()).subtleEffects$setFluidPair(null);
        });

        FLUID_PAIRS.clear();
        List<Fluid> sourceFluids = new ArrayList<>();
        List<Fluid> flowingFluids = new ArrayList<>();

        resources.forEach((id, element) ->
                FluidPair.CODEC.parse(JsonOps.INSTANCE, element)
                        .resultOrPartial(error -> SubtleEffects.LOGGER.error("Failed to decode fluid pair with ID {} - Error: {}", id, error))
                        .ifPresent(fluidPair -> load(id, fluidPair, sourceFluids, flowingFluids))
        );
    }

    private static void load(ResourceLocation id, FluidPair fluidPair, List<Fluid> sourceFluids, List<Fluid> flowingFluids) {
        Fluid source = fluidPair.source();
        Fluid flowing = fluidPair.flowing();

        if (source.isSame(Fluids.EMPTY) || flowing.isSame(Fluids.EMPTY)) {
            SubtleEffects.LOGGER.error("Fluid pair '{}' has empty fluid(s)", id);
            return;
        }

        if (sourceFluids.contains(source)) {
            SubtleEffects.LOGGER.error("Found duplicate source fluid in fluid pair: {}", id);
            return;
        }
        else if (flowingFluids.contains(flowing)) {
            SubtleEffects.LOGGER.error("Found duplicate flowing fluid in fluid pair: {}", id);
            return;
        }

        ((FluidAccessor) source).subtleEffects$setFluidPair(fluidPair);
        ((FluidAccessor) flowing).subtleEffects$setFluidPair(fluidPair);

        FLUID_PAIRS.put(id, fluidPair);
    }

    @Override
    public ResourceLocation getId() {
        return SubtleEffects.loc("fluid_pairs");
    }
}
