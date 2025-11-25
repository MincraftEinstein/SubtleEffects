package einstein.subtle_effects.data.color_providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.List;

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
        LAVA_DROPLET("lava_droplet", new ListColorProvider(List.of(new ConstantColorProvider(0xEEBA4E), new ConstantColorProvider(0xE48F30), new ConstantColorProvider(0xD96415)))),
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
