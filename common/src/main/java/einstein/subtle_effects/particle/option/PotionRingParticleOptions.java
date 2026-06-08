package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PotionRingParticleOptions(ParticleType<PotionRingParticleOptions> type,
                                        ColorProviderType.ColorProvider provider,
                                        int entityId) implements ParticleOptions {

    public static MapCodec<PotionRingParticleOptions> codec(ParticleType<PotionRingParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ColorProviderType.CODEC.fieldOf("color").forGetter(PotionRingParticleOptions::provider),
                Codec.INT.fieldOf("entityId").forGetter(PotionRingParticleOptions::entityId)
        ).apply(instance, (color, entityId) -> new PotionRingParticleOptions(type, color, entityId)));
    }

    public static StreamCodec<? super ByteBuf, PotionRingParticleOptions> streamCodec(ParticleType<PotionRingParticleOptions> type) {
        return StreamCodec.composite(
                ColorProviderType.STREAM_CODEC, PotionRingParticleOptions::provider,
                ByteBufCodecs.INT, PotionRingParticleOptions::entityId,
                (color, entityId) -> new PotionRingParticleOptions(type, color, entityId)
        );
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
