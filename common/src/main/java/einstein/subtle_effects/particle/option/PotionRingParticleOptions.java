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
                                        boolean isHarmful, int entityId) implements ParticleOptions {

    public static MapCodec<PotionRingParticleOptions> codec(ParticleType<PotionRingParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ColorProviderType.CODEC.fieldOf("color").forGetter(PotionRingParticleOptions::provider),
                Codec.BOOL.fieldOf("is_harmful").forGetter(PotionRingParticleOptions::isHarmful),
                Codec.INT.fieldOf("entity_id").forGetter(PotionRingParticleOptions::entityId)
        ).apply(instance, (color, isHarmful, entityId) -> new PotionRingParticleOptions(type, color, isHarmful, entityId)));
    }

    public static StreamCodec<? super ByteBuf, PotionRingParticleOptions> streamCodec(ParticleType<PotionRingParticleOptions> type) {
        return StreamCodec.composite(
                ColorProviderType.STREAM_CODEC, PotionRingParticleOptions::provider,
                ByteBufCodecs.BOOL, PotionRingParticleOptions::isHarmful,
                ByteBufCodecs.INT, PotionRingParticleOptions::entityId,
                (color, isHarmful, entityId) -> new PotionRingParticleOptions(type, color, isHarmful, entityId)
        );
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
