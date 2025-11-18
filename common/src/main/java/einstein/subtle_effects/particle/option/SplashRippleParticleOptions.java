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

public record SplashRippleParticleOptions(ResourceLocation type, float xScale) implements ParticleOptions {

    public static MapCodec<SplashRippleParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("type").forGetter(SplashRippleParticleOptions::type),
            Codec.FLOAT.fieldOf("xScale").forGetter(SplashRippleParticleOptions::xScale)
    ).apply(instance, SplashRippleParticleOptions::new));

    public static StreamCodec<FriendlyByteBuf, SplashRippleParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, SplashRippleParticleOptions::type,
            ByteBufCodecs.FLOAT, SplashRippleParticleOptions::xScale,
            SplashRippleParticleOptions::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticles.SPLASH_RIPPLE.get();
    }
}
