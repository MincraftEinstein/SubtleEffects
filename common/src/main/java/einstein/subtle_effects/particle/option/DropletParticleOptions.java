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

public record DropletParticleOptions(ResourceLocation fluidDefinitionId, boolean fromSplash,
                                     float scale, boolean isSilent) implements ParticleOptions {

    public static final MapCodec<DropletParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("fluid_definition").forGetter(DropletParticleOptions::fluidDefinitionId),
            Codec.BOOL.fieldOf("from_splash").forGetter(DropletParticleOptions::fromSplash),
            Codec.FLOAT.fieldOf("scale").forGetter(DropletParticleOptions::scale),
            Codec.BOOL.fieldOf("is_silent").forGetter(DropletParticleOptions::isSilent)
    ).apply(instance, DropletParticleOptions::new));

    public static final StreamCodec<FriendlyByteBuf, DropletParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, DropletParticleOptions::fluidDefinitionId,
            ByteBufCodecs.BOOL, DropletParticleOptions::fromSplash,
            ByteBufCodecs.FLOAT, DropletParticleOptions::scale,
            ByteBufCodecs.BOOL, DropletParticleOptions::isSilent,
            DropletParticleOptions::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticles.DROPLET.get();
    }
}
