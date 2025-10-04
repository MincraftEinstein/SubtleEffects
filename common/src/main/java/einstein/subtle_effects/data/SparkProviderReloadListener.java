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
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class SparkProviderReloadListener extends SimpleJsonResourceReloadListener implements NamedReloadListener {

    public static final String DIRECTORY = "subtle_effects/spark_providers";
    public static final Map<Block, List<SparkProvider>> PROVIDERS = new HashMap<>();

    public SparkProviderReloadListener() {
        super(Util.GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller filler) {
        PROVIDERS.clear();

        resources.forEach((id, element) ->
                SparkProviderData.CODEC.parse(JsonOps.INSTANCE, element)
                        .resultOrPartial(error -> SubtleEffects.LOGGER.error("Failed to decode spark provider with ID {} - Error: {}", id, error))
                        .ifPresent(provider -> provider.options().ifPresent(options -> load(id, provider)))
        );
    }

    private static void load(ResourceLocation location, SparkProviderData providerData) {
        Optional<SparkProviderData.Options> providerOptions = providerData.options();
        if (providerOptions.isEmpty()) {
            return;
        }

        SparkProviderData.Options options = providerOptions.get();
        providerData.states().forEach(providerEntry -> {
            ResourceLocation blockId = providerEntry.id();
            boolean isRequired = providerEntry.required();
            boolean isRegistered = BuiltInRegistries.BLOCK.containsKey(blockId);

            if (isRequired && !isRegistered) {
                SubtleEffects.LOGGER.warn("Could not find required block for states '{}' in Spark Provider: '{}'", blockId, location);
                return;
            }

            if (isRequired || isRegistered) {
                Block block = BuiltInRegistries.BLOCK.get(blockId);
                if (block.defaultBlockState().isAir()) {
                    SubtleEffects.LOGGER.error("Block in Spark Provider '{}' can not be air", location);
                }

                StateDefinition<Block, BlockState> definition = block.getStateDefinition();
                BlockStateHolder stateHolder = new BlockStateHolder(block, isRequired, providerEntry.properties()
                        .entrySet().stream()
                        .map(entry -> convertToProperties(entry, definition.getProperty(entry.getKey())))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1))
                );

                if (PROVIDERS.containsKey(block)) {
                    PROVIDERS.get(block).add(new SparkProvider(stateHolder, options));
                    return;
                }

                List<SparkProvider> providers = new ArrayList<>();
                providers.add(new SparkProvider(stateHolder, options));
                PROVIDERS.put(block, providers);
            }
        });
    }

    @Nullable
    private static <T extends Comparable<T>> Map.Entry<Property<T>, Comparable<T>> convertToProperties(Map.Entry<String, String> entry, @Nullable Property<T> property) {
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

    @Override
    public ResourceLocation getId() {
        return SubtleEffects.loc("spark_providers");
    }

    public record BlockStateHolder(Block block, boolean required, Map<Property<?>, Comparable<?>> properties) {

        public boolean matches(@Nullable BlockState state) {
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

    public record SparkProvider(BlockStateHolder stateHolder, SparkProviderData.Options options) {

    }
}
