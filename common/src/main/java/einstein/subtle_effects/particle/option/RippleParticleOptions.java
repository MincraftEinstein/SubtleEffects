package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record RippleParticleOptions(ParticleType<RippleParticleOptions> type, ResourceLocation fluidPairId,
                                    float scale, boolean fromSplash) implements ParticleOptions {

    public static MapCodec<RippleParticleOptions> codec(ParticleType<RippleParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                net.minecraft.resources.ResourceLocation.CODEC.fieldOf("fluid_pair").forGetter(RippleParticleOptions::fluidPairId),
                Codec.FLOAT.fieldOf("scale").forGetter(RippleParticleOptions::scale),
                Codec.BOOL.fieldOf("from_splash").forGetter(RippleParticleOptions::fromSplash)
        ).apply(instance, (fluidPairId, scale, fromSplash) -> new RippleParticleOptions(type, fluidPairId, scale, fromSplash)));
    }

    public static StreamCodec<RegistryFriendlyByteBuf, RippleParticleOptions> streamCodec(ParticleType<RippleParticleOptions> type) {
        return StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, RippleParticleOptions::fluidPairId,
                ByteBufCodecs.FLOAT, RippleParticleOptions::scale,
                ByteBufCodecs.BOOL, RippleParticleOptions::fromSplash,
                (fluidPairId, scale, fromSplash) -> new RippleParticleOptions(type, fluidPairId, scale, fromSplash)
        );
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
