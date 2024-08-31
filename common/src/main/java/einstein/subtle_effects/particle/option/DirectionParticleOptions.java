package einstein.subtle_effects.particle.option;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record DirectionParticleOptions(Direction direction) implements ParticleOptions {

    public static final MapCodec<DirectionParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Direction.CODEC.fieldOf("direction").forGetter(DirectionParticleOptions::direction)
    ).apply(instance, DirectionParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DirectionParticleOptions> STREAM_CODEC = StreamCodec.composite(
            Direction.STREAM_CODEC, DirectionParticleOptions::direction,
            DirectionParticleOptions::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticles.COMMAND_BLOCK.get();
    }
}
