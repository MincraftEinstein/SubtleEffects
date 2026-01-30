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

public record SplashParticleOptions(ResourceLocation fluidDefinitionId, float horizontalScale,
                                    float verticalScale) implements ParticleOptions {

    public static MapCodec<SplashParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("fluid_definition").forGetter(SplashParticleOptions::fluidDefinitionId),
            Codec.FLOAT.fieldOf("horizontalScale").forGetter(SplashParticleOptions::horizontalScale),
            Codec.FLOAT.fieldOf("verticalScale").forGetter(SplashParticleOptions::verticalScale)
    ).apply(instance, SplashParticleOptions::new));

    public static StreamCodec<FriendlyByteBuf, SplashParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, SplashParticleOptions::fluidDefinitionId,
            ByteBufCodecs.FLOAT, SplashParticleOptions::horizontalScale,
            ByteBufCodecs.FLOAT, SplashParticleOptions::verticalScale,
            SplashParticleOptions::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticles.SPLASH.get();
    }
}
