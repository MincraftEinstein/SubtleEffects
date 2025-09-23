package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record ColorAndIntegerParticleOptions(ParticleType<ColorAndIntegerParticleOptions> type, int color, int integer) implements ParticleOptions {

    public static MapCodec<ColorAndIntegerParticleOptions> codec(ParticleType<ColorAndIntegerParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("color").forGetter(ColorAndIntegerParticleOptions::color),
                Codec.INT.fieldOf("integer").forGetter(ColorAndIntegerParticleOptions::integer)
        ).apply(instance, (color, integer) -> new ColorAndIntegerParticleOptions(type, color, integer)));
    }

    public static StreamCodec<? super ByteBuf, ColorAndIntegerParticleOptions> streamCodec(ParticleType<ColorAndIntegerParticleOptions> type) {
        return StreamCodec.composite(
                ByteBufCodecs.INT, ColorAndIntegerParticleOptions::color,
                ByteBufCodecs.INT, ColorAndIntegerParticleOptions::integer,
                (color, integer) -> new ColorAndIntegerParticleOptions(type, color, integer)
        );
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
