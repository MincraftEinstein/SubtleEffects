package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FloatAndIntegerParticleOptions(ParticleType<FloatAndIntegerParticleOptions> type, float f,
                                             int integer) implements ParticleOptions {

    public static MapCodec<FloatAndIntegerParticleOptions> codec(ParticleType<FloatAndIntegerParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.FLOAT.fieldOf("float").forGetter(FloatAndIntegerParticleOptions::f),
                Codec.INT.fieldOf("integer").forGetter(FloatAndIntegerParticleOptions::integer)
        ).apply(instance, (f, integer) -> new FloatAndIntegerParticleOptions(type, f, integer)));
    }

    public static StreamCodec<ByteBuf, FloatAndIntegerParticleOptions> streamCodec(ParticleType<FloatAndIntegerParticleOptions> type) {
        return StreamCodec.composite(
                ByteBufCodecs.FLOAT, FloatAndIntegerParticleOptions::f,
                ByteBufCodecs.INT, FloatAndIntegerParticleOptions::integer,
                (f, integer) -> new FloatAndIntegerParticleOptions(type, f, integer)
        );
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
