package einstein.subtle_effects.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.util.Box;
import einstein.subtle_effects.util.SparkType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Collectors;

public record SparkProvider(List<BlockStateHolder> states, Optional<Options> options) {

    public static final Codec<SparkProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(BlockStateHolder.CODEC, 1, Integer.MAX_VALUE).fieldOf("states").forGetter(SparkProvider::states),
            Options.CODEC.optionalFieldOf("options").forGetter(SparkProvider::options)
    ).apply(instance, SparkProvider::new));

    public record BlockStateHolder(Block block, Map<Property<?>, Comparable<?>> properties) {

        public static final Codec<BlockStateHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("name").forGetter(BlockStateHolder::block),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties").forGetter(holder ->
                        Optional.of(holder.properties.entrySet().stream()
                                .collect(Collectors.toMap(entry -> entry.getKey().getName(), entry -> entry.getValue().toString(), (s1, s2) -> s1)))
                )
        ).apply(instance, (block, properties) -> {
            StateDefinition<Block, BlockState> definition = block.getStateDefinition();
            return new BlockStateHolder(block, properties.orElse(new HashMap<>())
                    .entrySet().stream()
                    .map(entry -> convertToProperties(entry, definition.getProperty(entry.getKey())))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1)));
        }));

        private static <T extends Comparable<T>> Map.Entry<Property<T>, Comparable<T>> convertToProperties(Map.Entry<String, String> entry, Property<T> property) {
            String propertyName = entry.getKey();
            String valueName = entry.getValue();

            if (property != null) {
                Optional<T> value = property.getValue(valueName);
                if (value.isPresent()) {
                    return Map.entry(property, value.get());
                }

                SubtleEffects.LOGGER.error("Unknown value '{}' for blockstate property '{}' in spark provider. Possible values for property {}", valueName, propertyName, property.getPossibleValues());
                return null;
            }

            SubtleEffects.LOGGER.error("Unknown blockstate property '{}' for spark provider", propertyName);
            return null;
        }

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

    public record Options(PresetType preset, Optional<SparkType> sparkType, Optional<Box> box,
                          Optional<IntProvider> count, Optional<Float> chance, Optional<Vec3> velocity,
                          Optional<List<Integer>> colors) {

        public static final Codec<Options> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                PresetType.CODEC.fieldOf("preset").forGetter(Options::preset),
                SparkType.CODEC.optionalFieldOf("spark").forGetter(Options::sparkType),
                Box.CODEC.optionalFieldOf("box").forGetter(Options::box),
                IntProvider.CODEC.optionalFieldOf("count").forGetter(Options::count),
                Codec.floatRange(0, 1).optionalFieldOf("chance").forGetter(Options::chance),
                Vec3.CODEC.optionalFieldOf("velocity").forGetter(Options::velocity),
                Codec.withAlternative(Codec.list(Codec.INT), Codec.STRING, string -> {
                    if (string.equals("soul")) {
                        return SparkParticle.SOUL_COLORS;
                    }
                    else if (string.equals("default")) {
                        return SparkParticle.DEFAULT_COLORS;
                    }

                    SubtleEffects.LOGGER.error("Spark color must be an integer array or string of either 'soul' or 'default'");
                    return null;
                }).optionalFieldOf("colors").forGetter(Options::colors)
        ).apply(instance, Options::new));
    }

    public enum PresetType implements StringRepresentable {

        CUSTOM("custom"),
        CAMPFIRE("campfire"),
        FIRE("fire"),
        TORCH("torch"),
        WALL_TORCH("wall_torch"),
        CANDLE("candle"),
        LANTERN("lantern"),
        FURNACE("furnace");

        public static final Codec<PresetType> CODEC = StringRepresentable.fromEnum(PresetType::values);

        private final String name;

        PresetType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
