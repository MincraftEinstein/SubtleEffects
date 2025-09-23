package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FloatParticleOptions(ParticleType<FloatParticleOptions> type, float f) implements ParticleOptions {

    public static MapCodec<FloatParticleOptions> codec(ParticleType<FloatParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.FLOAT.fieldOf("float").forGetter(FloatParticleOptions::f)
        ).apply(instance, f -> new FloatParticleOptions(type, f)));
    }

    public static StreamCodec<ByteBuf, FloatParticleOptions> streamCodec(ParticleType<FloatParticleOptions> type) {
        return ByteBufCodecs.FLOAT.map(f -> new FloatParticleOptions(type, f), FloatParticleOptions::f);
    }

    @Override
    public ParticleType<?> getType() {
        return type();
    }
}
