package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BooleanParticleOptions(ParticleType<?> type, boolean bool) implements ParticleOptions {

    public static MapCodec<BooleanParticleOptions> codec(ParticleType<BooleanParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.BOOL.fieldOf("boolean").forGetter(BooleanParticleOptions::bool)
        ).apply(instance, bool -> new BooleanParticleOptions(type, bool)));
    }

    public static StreamCodec<ByteBuf, BooleanParticleOptions> streamCodec(ParticleType<BooleanParticleOptions> type) {
        return ByteBufCodecs.BOOL.map(bool -> new BooleanParticleOptions(type, bool), BooleanParticleOptions::bool);
    }

    @Override
    public ParticleType<?> getType() {
        return type();
    }
}
