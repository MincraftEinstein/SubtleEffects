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

public record SplashDropletParticleOptions(ResourceLocation fluidPairId, float scale) implements ParticleOptions {

    public static final MapCodec<SplashDropletParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("fluid_pair").forGetter(SplashDropletParticleOptions::fluidPairId),
            Codec.FLOAT.fieldOf("scale").forGetter(SplashDropletParticleOptions::scale)
    ).apply(instance, SplashDropletParticleOptions::new));

    public static final StreamCodec<FriendlyByteBuf, SplashDropletParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, SplashDropletParticleOptions::fluidPairId,
            ByteBufCodecs.FLOAT, SplashDropletParticleOptions::scale,
            SplashDropletParticleOptions::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticles.SPLASH_DROPLET.get();
    }
}
