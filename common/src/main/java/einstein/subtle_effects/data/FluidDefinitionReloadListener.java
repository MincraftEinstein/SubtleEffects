package einstein.subtle_effects.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.data.splash_types.SplashType;
import einstein.subtle_effects.data.splash_types.SplashTypeReloadListener;
import einstein.subtle_effects.util.BucketItemAccessor;
import einstein.subtle_effects.util.FluidDefinitionAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.*;

import static einstein.subtle_effects.SubtleEffects.LOGGER;

public class FluidDefinitionReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, FluidDefinition.Data>> implements NamedReloadListener {

    public static final ResourceLocation WATER_ID = new ResourceLocation("water");
    public static final ResourceLocation LAVA_ID = new ResourceLocation("lava");
    private static final String DIRECTORY = "subtle_effects/fluid_definitions";
    public static final Map<ResourceLocation, FluidDefinition> DEFINITIONS = new HashMap<>();

    @Override
    protected Map<ResourceLocation, FluidDefinition.Data> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, JsonElement> resources = new HashMap<>();
        List<Fluid> sourceFluids = new ArrayList<>();
        List<Fluid> flowingFluids = new ArrayList<>();
        List<AbstractCauldronBlock> cauldrons = new ArrayList<>();
        List<BucketItem> bucketItems = new ArrayList<>();
        Map<ResourceLocation, FluidDefinition.Data> definitions = new HashMap<>();

        SimpleJsonResourceReloadListener.scanDirectory(resourceManager, DIRECTORY, Util.GSON, resources);
        resources.forEach((id, element) ->
                FluidDefinition.Data.CODEC.parse(JsonOps.INSTANCE, element)
                        .resultOrPartial(error -> LOGGER.error("Failed to decode fluid definition with ID {} - Error: {}", id, error))
                        .ifPresent(data -> validate(id, data, sourceFluids, flowingFluids, cauldrons, bucketItems, definitions))
        );
        return definitions;
    }

    @Override
    protected void apply(Map<ResourceLocation, FluidDefinition.Data> resources, ResourceManager manager, ProfilerFiller profiler) {
        DEFINITIONS.forEach((id, fluidDefinition) -> {
            ((FluidDefinitionAccessor) fluidDefinition.source()).subtleEffects$setFluidDefinition(null);
            ((FluidDefinitionAccessor) fluidDefinition.flowing()).subtleEffects$setFluidDefinition(null);
            fluidDefinition.cauldron().ifPresent(cauldron -> ((FluidDefinitionAccessor) cauldron).subtleEffects$setFluidDefinition(fluidDefinition));
        });

        DEFINITIONS.clear();

        resources.forEach((id, data) -> {
            Fluid source = data.source();
            Fluid flowing = data.flowing();
            FluidDefinition fluidDefinition = new FluidDefinition(id, source, flowing, data.cauldron(),
                    data.splashTypeId().map(splashTypeId -> {
                        SplashType splashType = SplashTypeReloadListener.SPLASH_TYPES.get(splashTypeId);
                        if (splashType != null) {
                            return splashType;
                        }

                        LOGGER.error("Splash type '{}' not found for fluid definition '{}'", splashTypeId, id);
                        return null;
                    }),
                    data.bucketItem(), data.dropletOptions(), data.lightEmission()
            );

            ((FluidDefinitionAccessor) source).subtleEffects$setFluidDefinition(fluidDefinition);
            ((FluidDefinitionAccessor) flowing).subtleEffects$setFluidDefinition(fluidDefinition);
            data.cauldron().ifPresent(cauldron -> ((FluidDefinitionAccessor) cauldron).subtleEffects$setFluidDefinition(fluidDefinition));
            DEFINITIONS.put(id, fluidDefinition);
        });
    }

    private static void validate(ResourceLocation id, FluidDefinition.Data data, List<Fluid> sourceFluids, List<Fluid> flowingFluids, List<AbstractCauldronBlock> cauldrons, List<BucketItem> bucketItems, Map<ResourceLocation, FluidDefinition.Data> definitions) {
        Fluid source = data.source();
        Fluid flowing = data.flowing();

        if (source.isSame(Fluids.EMPTY) || flowing.isSame(Fluids.EMPTY)) {
            LOGGER.error("Fluid definition '{}' has empty fluid(s)", id);
            return;
        }

        if (sourceFluids.contains(source)) {
            LOGGER.error("Found duplicate source fluid in fluid definition: '{}'", id);
            return;
        }
        else if (flowingFluids.contains(flowing)) {
            LOGGER.error("Found duplicate flowing fluid in fluid definition: '{}'", id);
            return;
        }
        else if (data.cauldron().filter(cauldrons::contains).isPresent()) {
            LOGGER.error("Found duplicate cauldron in fluid definition: '{}'", id);
            return;
        }

        Optional<BucketItem> bucketItem = data.bucketItem();
        if (bucketItem.isPresent()) {
            Fluid fluid = ((BucketItemAccessor) bucketItem.get()).getContent();
            if (!fluid.isSame(source) || !fluid.isSame(flowing)) {
                LOGGER.error("Bucket item contents '{}' does not match fluid definition '{}'", BuiltInRegistries.ITEM.getKey(bucketItem.get()), id);
                return;
            }
        }

        sourceFluids.add(source);
        flowingFluids.add(flowing);
        data.cauldron().ifPresent(cauldrons::add);
        bucketItem.ifPresent(bucketItems::add);
        definitions.put(id, data);
    }

    @Override
    public ResourceLocation getId() {
        return SubtleEffects.loc("fluid_definitions");
    }
}
