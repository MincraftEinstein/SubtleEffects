package einstein.subtle_effects.particle.option;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;

public record DirectionParticleOptions(ParticleType<?> type, Direction direction) implements ParticleOptions {

    public static MapCodec<DirectionParticleOptions> codec(ParticleType<DirectionParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                net.minecraft.core.Direction.CODEC.fieldOf("direction").forGetter(DirectionParticleOptions::direction)
        ).apply(instance, direction -> new DirectionParticleOptions(type, direction)));
    }

    public static StreamCodec<ByteBuf, DirectionParticleOptions> streamCodec(ParticleType<DirectionParticleOptions> type) {
        return Direction.STREAM_CODEC.map(direction -> new DirectionParticleOptions(type, direction), DirectionParticleOptions::direction);
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
