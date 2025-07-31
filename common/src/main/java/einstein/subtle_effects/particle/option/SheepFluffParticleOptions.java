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
import net.minecraft.world.item.DyeColor;

public record SheepFluffParticleOptions(DyeColor color, int sheepId, boolean isJeb) implements ParticleOptions {

    public static MapCodec<SheepFluffParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DyeColor.CODEC.fieldOf("color").forGetter(SheepFluffParticleOptions::color),
            Codec.INT.fieldOf("sheepId").forGetter(SheepFluffParticleOptions::sheepId),
            Codec.BOOL.fieldOf("isJeb").forGetter(SheepFluffParticleOptions::isJeb)
    ).apply(instance, SheepFluffParticleOptions::new));

    public static StreamCodec<ByteBuf, SheepFluffParticleOptions> STREAM_CODEC = StreamCodec.composite(
            DyeColor.STREAM_CODEC, SheepFluffParticleOptions::color,
            ByteBufCodecs.INT, SheepFluffParticleOptions::sheepId,
            ByteBufCodecs.BOOL, SheepFluffParticleOptions::isJeb,
            SheepFluffParticleOptions::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticles.SHEEP_FLUFF.get();
    }
}
