package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SplashEmitterParticleOptions(ParticleType<SplashEmitterParticleOptions> type, float widthModifier,
                                           float heightModifier, float velocity,
                                           int entityId) implements ParticleOptions {

    public static MapCodec<SplashEmitterParticleOptions> codec(ParticleType<SplashEmitterParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                        Codec.FLOAT.fieldOf("widthModifier").forGetter(SplashEmitterParticleOptions::widthModifier),
                        Codec.FLOAT.fieldOf("heightModifier").forGetter(SplashEmitterParticleOptions::heightModifier),
                        Codec.FLOAT.fieldOf("velocity").forGetter(SplashEmitterParticleOptions::velocity),
                        Codec.INT.fieldOf("entityId").forGetter(SplashEmitterParticleOptions::entityId)
                ).apply(instance, (widthModifier, heightModifier, velocity, entityId) ->
                        new SplashEmitterParticleOptions(type, widthModifier, heightModifier, velocity, entityId))
        );
    }

    public static StreamCodec<ByteBuf, SplashEmitterParticleOptions> streamCodec(ParticleType<SplashEmitterParticleOptions> type) {
        return StreamCodec.composite(
                ByteBufCodecs.FLOAT, SplashEmitterParticleOptions::widthModifier,
                ByteBufCodecs.FLOAT, SplashEmitterParticleOptions::heightModifier,
                ByteBufCodecs.FLOAT, SplashEmitterParticleOptions::velocity,
                ByteBufCodecs.INT, SplashEmitterParticleOptions::entityId,
                (widthModifier, heightModifier, velocity, entityId) ->
                        new SplashEmitterParticleOptions(type, widthModifier, heightModifier, velocity, entityId)
        );
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
