package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.ticking.tickers.geyser.GeyserType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GeyserSpoutParticleOptions(GeyserType type, int lifeTime) implements ParticleOptions {

    public static final MapCodec<GeyserSpoutParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            GeyserType.CODEC.fieldOf("type").forGetter(GeyserSpoutParticleOptions::type),
            Codec.INT.fieldOf("lifeTime").forGetter(GeyserSpoutParticleOptions::lifeTime)
    ).apply(instance, GeyserSpoutParticleOptions::new));

    public static final StreamCodec<FriendlyByteBuf, GeyserSpoutParticleOptions> STREAM_CODEC = StreamCodec.composite(
            GeyserType.STREAM_CODEC, GeyserSpoutParticleOptions::type,
            ByteBufCodecs.INT, GeyserSpoutParticleOptions::lifeTime,
            GeyserSpoutParticleOptions::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticles.GEYSER_SPOUT.get();
    }
}
