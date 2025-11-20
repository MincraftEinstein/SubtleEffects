package einstein.subtle_effects.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import java.util.Optional;

public record FluidPair(Fluid source, Fluid flowing, Optional<AbstractCauldronBlock> cauldron) {

    public static final Codec<FluidPair> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("source").forGetter(FluidPair::source),
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("flowing").forGetter(FluidPair::flowing),
            BuiltInRegistries.BLOCK.byNameCodec().comapFlatMap(block -> {
                if (block instanceof AbstractCauldronBlock cauldron) {
                    return DataResult.success(cauldron);
                }
                return DataResult.error(() -> "Block is not a cauldron: '" + BuiltInRegistries.BLOCK.getKey(block) + "'");
            }, a -> a).optionalFieldOf("cauldron").forGetter(FluidPair::cauldron)
    ).apply(instance, FluidPair::new));

    public boolean is(Fluid fluid) {
        return fluid == source || fluid == flowing;
    }

    public boolean is(FluidState state) {
        Fluid type = state.getType();
        return type == source || type == flowing;
    }

    public boolean is(BlockState state) {
        return cauldron.filter(cauldronBlock -> state.getBlock() == cauldronBlock).isPresent();
    }
}
