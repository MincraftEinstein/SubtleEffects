package einstein.subtle_effects.data.color_providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.particle.SparkParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.List;

import static einstein.subtle_effects.data.color_providers.ListColorProvider.fromIntList;

public record PresetColorProvider(Preset preset) implements ColorProviderType.ColorProvider {

    public static final MapCodec<PresetColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Preset.CODEC.fieldOf("preset").forGetter(PresetColorProvider::preset)
    ).apply(instance, PresetColorProvider::new));

    @Override
    public ColorProviderType<?> getType() {
        return ColorProviderType.PRESET;
    }

    @Override
    public Vector3f provideColor(Level level, BlockPos pos, RandomSource random) {
        return preset.color.provideColor(level, pos, random);
    }

    public enum Preset implements StringRepresentable {
        SPARKS("sparks", fromIntList(SparkParticle.DEFAULT_COLORS)),
        SOUL_SPARKS("soul_sparks", fromIntList(SparkParticle.SOUL_COLORS)),
        BLAZE_SPARKS("blaze_sparks", fromIntList(SparkParticle.BLAZE_COLORS)),
        COPPER_SPARKS("copper_sparks", fromIntList(SparkParticle.COPPER_COLORS)),
        LAVA_DROPLET("lava_droplet", fromIntList(List.of(0xEEBA4E, 0xE48F30, 0xD96415))),
        LAVA_RIPPLE("lava_ripple", new ConstantColorProvider(0xDE7A22));

        public static final Codec<Preset> CODEC = StringRepresentable.fromValues(Preset::values);

        private final String name;
        private final ColorProviderType.ColorProvider color;

        Preset(String name, ColorProviderType.ColorProvider color) {
            this.name = name;
            this.color = color;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
