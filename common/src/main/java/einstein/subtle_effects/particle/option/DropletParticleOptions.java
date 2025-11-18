package einstein.subtle_effects.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DropletParticleOptions(ParticleType<DropletParticleOptions> type, float scale,
                                     float colorIntensity, boolean isSilent) implements ParticleOptions {

    public DropletParticleOptions(ParticleType<DropletParticleOptions> type, float scale, float colorIntensity) {
        this(type, scale, colorIntensity, false);
    }

    public DropletParticleOptions(ParticleType<DropletParticleOptions> type, float scale) {
        this(type, scale, 1);
    }

    public static MapCodec<DropletParticleOptions> codec(ParticleType<DropletParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.FLOAT.fieldOf("scale").forGetter(DropletParticleOptions::scale),
                Codec.FLOAT.fieldOf("colorIntensity").forGetter(DropletParticleOptions::colorIntensity),
                Codec.BOOL.fieldOf("isSilent").forGetter(DropletParticleOptions::isSilent)
        ).apply(instance, (scale, colorIntensity, isSilent) -> new DropletParticleOptions(type, scale, colorIntensity, isSilent)));
    }

    public static StreamCodec<ByteBuf, DropletParticleOptions> streamCodec(ParticleType<DropletParticleOptions> type) {
        return StreamCodec.composite(
                ByteBufCodecs.FLOAT, DropletParticleOptions::scale,
                ByteBufCodecs.FLOAT, DropletParticleOptions::colorIntensity,
                ByteBufCodecs.BOOL, DropletParticleOptions::isSilent,
                (scale, colorIntensity, isSilent) -> new DropletParticleOptions(type, scale, colorIntensity, isSilent)
        );
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
