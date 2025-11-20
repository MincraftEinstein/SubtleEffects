package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record SplashParticleOptions(ResourceLocation fluidPairId, float xScale,
                                    float yScale) implements ParticleOptions {

    public static MapCodec<SplashParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("fluid_pair").forGetter(SplashParticleOptions::fluidPairId),
            Codec.FLOAT.fieldOf("xScale").forGetter(SplashParticleOptions::xScale),
            Codec.FLOAT.fieldOf("yScale").forGetter(SplashParticleOptions::yScale)
    ).apply(instance, SplashParticleOptions::new));

    public static StreamCodec<FriendlyByteBuf, SplashParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, SplashParticleOptions::fluidPairId,
            ByteBufCodecs.FLOAT, SplashParticleOptions::xScale,
            ByteBufCodecs.FLOAT, SplashParticleOptions::yScale,
            SplashParticleOptions::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticles.SPLASH.get();
    }
}
