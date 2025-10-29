package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SplashParticleOptions(ParticleType<SplashParticleOptions> type, float xScale,
                                    float yScale) implements ParticleOptions {

    public static MapCodec<SplashParticleOptions> codec(ParticleType<SplashParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.FLOAT.fieldOf("xScale").forGetter(SplashParticleOptions::xScale),
                Codec.FLOAT.fieldOf("yScale").forGetter(SplashParticleOptions::yScale)
        ).apply(instance, (xScale, yScale) -> new SplashParticleOptions(type, xScale, yScale)));
    }

    public static StreamCodec<FriendlyByteBuf, SplashParticleOptions> streamCodec(ParticleType<SplashParticleOptions> type) {
        return StreamCodec.composite(
                ByteBufCodecs.FLOAT, SplashParticleOptions::xScale,
                ByteBufCodecs.FLOAT, SplashParticleOptions::yScale,
                (xScale, yScale) -> new SplashParticleOptions(type, xScale, yScale)
        );
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
