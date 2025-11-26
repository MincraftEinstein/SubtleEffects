package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.init.ModParticles;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record SplashEmitterParticleOptions(ResourceLocation fluidDefinitionId, float widthModifier,
                                           float heightModifier, float velocity,
                                           int entityId) implements ParticleOptions {

    public static final MapCodec<SplashEmitterParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("fluid_definition").forGetter(SplashEmitterParticleOptions::fluidDefinitionId),
                    Codec.FLOAT.fieldOf("widthModifier").forGetter(SplashEmitterParticleOptions::widthModifier),
                    Codec.FLOAT.fieldOf("heightModifier").forGetter(SplashEmitterParticleOptions::heightModifier),
                    Codec.FLOAT.fieldOf("velocity").forGetter(SplashEmitterParticleOptions::velocity),
                    Codec.INT.fieldOf("entityId").forGetter(SplashEmitterParticleOptions::entityId)
            ).apply(instance, SplashEmitterParticleOptions::new)
    );

    public static final StreamCodec<ByteBuf, SplashEmitterParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, SplashEmitterParticleOptions::fluidDefinitionId,
            ByteBufCodecs.FLOAT, SplashEmitterParticleOptions::widthModifier,
            ByteBufCodecs.FLOAT, SplashEmitterParticleOptions::heightModifier,
            ByteBufCodecs.FLOAT, SplashEmitterParticleOptions::velocity,
            ByteBufCodecs.INT, SplashEmitterParticleOptions::entityId,
            SplashEmitterParticleOptions::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticles.SPLASH_EMITTER.get();
    }
}
