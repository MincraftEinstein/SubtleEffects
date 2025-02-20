package einstein.subtle_effects.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.*;
import java.util.stream.Collectors;

public class SparkProviderReloadListener extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "subtle_effects/spark_providers";
    public static final Map<ResourceLocation, SparkProvider> SPARK_PROVIDERS = new HashMap<>();

    public SparkProviderReloadListener() {
        super(Util.GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller filler) {
        Map<ResourceLocation, SparkProviderData> dataMap = new HashMap<>();
        SPARK_PROVIDERS.clear();

        resources.forEach((id, element) ->
                SparkProviderData.CODEC.parse(JsonOps.INSTANCE, element)
                        .resultOrPartial(error -> SubtleEffects.LOGGER.error("Failed to decode spark provider with ID {} - Error: {}", id, error))
                        .ifPresent(provider -> provider.options().ifPresent(options -> dataMap.put(id, provider)))
        );

        load(dataMap);
    }

    private static void load(Map<ResourceLocation, SparkProviderData> dataMap) {
        dataMap.forEach((location, providerData) -> {
            Optional<SparkProviderData.Options> options = providerData.options();
            if (options.isEmpty()) {
                return;
            }

            List<BlockStateHolder> stateHolders = providerData.states().stream().map(providerEntry -> {
                ResourceLocation blockId = providerEntry.id();
                boolean isRequired = providerEntry.required();
                boolean isRegistered = BuiltInRegistries.BLOCK.containsKey(blockId);

                if (isRequired && !isRegistered) {
                    SubtleEffects.LOGGER.warn("Could not find required block for states '{}' in Spark Provider: '{}'", blockId, location);
                    return null;
                }

                if (isRequired || isRegistered) {
                    Block block = BuiltInRegistries.BLOCK.get(blockId);
                    if (block.defaultBlockState().isAir()) {
                        SubtleEffects.LOGGER.error("Block in Spark Provider '{}' can not be air", location);
                    }

                    StateDefinition<Block, BlockState> definition = block.getStateDefinition();
                    return new BlockStateHolder(block, isRequired, providerEntry.properties()
                            .entrySet().stream()
                            .map(entry -> convertToProperties(entry, definition.getProperty(entry.getKey())))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1))
                    );
                }
                return null;
            }).filter(Objects::nonNull).toList();

            SPARK_PROVIDERS.put(location, new SparkProvider(stateHolders, options.get()));
        });
    }

    private static <T extends Comparable<T>> Map.Entry<Property<T>, Comparable<T>> convertToProperties(Map.Entry<String, String> entry, Property<T> property) {
        String propertyName = entry.getKey();
        String valueName = entry.getValue();

        if (property != null) {
            Optional<T> value = property.getValue(valueName);
            if (value.isPresent()) {
                return Map.entry(property, value.get());
            }

            SubtleEffects.LOGGER.error("Unknown value '{}' for blockstate property '{}' in Spark Provider. Possible values for property {}", valueName, propertyName, property.getPossibleValues());
            return null;
        }

        SubtleEffects.LOGGER.error("Unknown blockstate property '{}' for Spark Provider", propertyName);
        return null;
    }

    public record BlockStateHolder(Block block, boolean required, Map<Property<?>, Comparable<?>> properties) {

        public boolean matches(BlockState state) {
            if (state != null && state.is(block)) {
                for (Map.Entry<Property<?>, Comparable<?>> entry : properties.entrySet()) {
                    if (!Objects.equals(state.getValue(entry.getKey()), entry.getValue())) {
                        return false;
                    }
                }

                return true;
            }
            return false;
        }
    }

    public record SparkProvider(List<BlockStateHolder> states, SparkProviderData.Options options) {

    }
}
