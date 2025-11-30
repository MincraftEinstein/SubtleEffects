package einstein.subtle_effects.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.util.Box;
import einstein.subtle_effects.util.SparkType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record SparkProviderData(List<BlockStateEntry> states, Optional<Options> options) {

    public static final Codec<SparkProviderData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(BlockStateEntry.CODEC, 1, Integer.MAX_VALUE).fieldOf("states").forGetter(SparkProviderData::states),
            Options.CODEC.optionalFieldOf("options").forGetter(SparkProviderData::options)
    ).apply(instance, SparkProviderData::new));

    public record BlockStateEntry(Identifier id, boolean required, Map<String, String> properties) {

        public static final Codec<BlockStateEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("name").forGetter(BlockStateEntry::id),
                Codec.BOOL.optionalFieldOf("required", true).forGetter(BlockStateEntry::required),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties", Map.of()).forGetter(BlockStateEntry::properties)
        ).apply(instance, BlockStateEntry::new));
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
                Codec.withAlternative(Codec.list(ExtraCodecs.RGB_COLOR_CODEC), Codec.STRING, string -> {
                    switch (string) {
                        case "soul" -> {
                            return SparkParticle.SOUL_COLORS;
                        }
                        case "copper" -> {
                            return SparkParticle.COPPER_COLORS;
                        }
                        case "default" -> {
                            return SparkParticle.DEFAULT_COLORS;
                        }
                    }

                    SubtleEffects.LOGGER.error("Spark color must be an integer array or string of either 'soul', 'copper' or 'default'");
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
