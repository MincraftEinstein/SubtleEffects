package einstein.subtle_effects.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;

public record FluidPair(Fluid source, Fluid flowing) {

    public static final Codec<FluidPair> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("source").forGetter(FluidPair::source),
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("flowing").forGetter(FluidPair::flowing)
    ).apply(instance, FluidPair::new));
}
