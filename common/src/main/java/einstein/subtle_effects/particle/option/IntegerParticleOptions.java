package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.init.ModParticles;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record IntegerParticleOptions(ParticleType<IntegerParticleOptions> type,
                                     int integer) implements ParticleOptions {

    public static MapCodec<IntegerParticleOptions> codec(ParticleType<IntegerParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.INT.fieldOf("integer").forGetter(IntegerParticleOptions::integer)
        ).apply(instance, i -> new IntegerParticleOptions(type, i)));
    }

    public static StreamCodec<ByteBuf, IntegerParticleOptions> streamCodec(ParticleType<IntegerParticleOptions> type) {
        return ByteBufCodecs.INT.map(i -> new IntegerParticleOptions(type, i), IntegerParticleOptions::integer);
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.SHEEP_FLUFF.get();
    }
}
