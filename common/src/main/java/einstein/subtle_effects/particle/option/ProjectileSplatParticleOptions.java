package einstein.subtle_effects.particle.option;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record ProjectileSplatParticleOptions(ParticleType<ProjectileSplatParticleOptions> type, Direction direction,
                                             BlockPos pos) implements ParticleOptions {

    public static MapCodec<ProjectileSplatParticleOptions> codec(ParticleType<ProjectileSplatParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Direction.CODEC.fieldOf("direction").forGetter(ProjectileSplatParticleOptions::direction),
                BlockPos.CODEC.fieldOf("pos").forGetter(ProjectileSplatParticleOptions::pos)
        ).apply(instance, (direction, pos) -> new ProjectileSplatParticleOptions(type, direction, pos)));
    }

    public static StreamCodec<FriendlyByteBuf, ProjectileSplatParticleOptions> streamCodec(ParticleType<ProjectileSplatParticleOptions> type) {
        return StreamCodec.composite(
                Direction.STREAM_CODEC, ProjectileSplatParticleOptions::direction,
                BlockPos.STREAM_CODEC, ProjectileSplatParticleOptions::pos,
                (direction, pos) -> new ProjectileSplatParticleOptions(type, direction, pos)
        );
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
