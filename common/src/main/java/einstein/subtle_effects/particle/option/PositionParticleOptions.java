package einstein.subtle_effects.particle.option;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;

public record PositionParticleOptions(ParticleType<PositionParticleOptions> type, BlockPos pos) implements ParticleOptions {

    public static MapCodec<PositionParticleOptions> codec(ParticleType<PositionParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(PositionParticleOptions::pos)
        ).apply(instance, pos -> new PositionParticleOptions(type, pos)));
    }

    public static StreamCodec<ByteBuf, PositionParticleOptions> streamCodec(ParticleType<PositionParticleOptions> type) {
        return BlockPos.STREAM_CODEC.map(pos -> new PositionParticleOptions(type, pos), PositionParticleOptions::pos);
    }

    @Override
    public ParticleType<?> getType() {
        return type();
    }
}
