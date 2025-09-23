package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SplashDropletParticleOptions(ParticleType<SplashDropletParticleOptions> type, float scale,
                                           float colorIntensity, boolean isSilent) implements ParticleOptions {

    public SplashDropletParticleOptions(ParticleType<SplashDropletParticleOptions> type, float scale, float colorIntensity) {
        this(type, scale, colorIntensity, false);
    }

    public SplashDropletParticleOptions(ParticleType<SplashDropletParticleOptions> type, float scale) {
        this(type, scale, 1);
    }

    public static MapCodec<SplashDropletParticleOptions> codec(ParticleType<SplashDropletParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.FLOAT.fieldOf("scale").forGetter(SplashDropletParticleOptions::scale),
                Codec.FLOAT.fieldOf("colorIntensity").forGetter(SplashDropletParticleOptions::colorIntensity),
                Codec.BOOL.fieldOf("isSilent").forGetter(SplashDropletParticleOptions::isSilent)
        ).apply(instance, (scale, colorIntensity, isSilent) -> new SplashDropletParticleOptions(type, scale, colorIntensity, isSilent)));
    }

    public static StreamCodec<ByteBuf, SplashDropletParticleOptions> streamCodec(ParticleType<SplashDropletParticleOptions> type) {
        return StreamCodec.composite(
                ByteBufCodecs.FLOAT, SplashDropletParticleOptions::scale,
                ByteBufCodecs.FLOAT, SplashDropletParticleOptions::colorIntensity,
                ByteBufCodecs.BOOL, SplashDropletParticleOptions::isSilent,
                (scale, colorIntensity, isSilent) -> new SplashDropletParticleOptions(type, scale, colorIntensity, isSilent)
        );
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
