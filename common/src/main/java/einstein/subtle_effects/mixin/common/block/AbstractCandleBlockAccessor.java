package einstein.subtle_effects.mixin.common.block;

import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractCandleBlock.class)
public interface AbstractCandleBlockAccessor {

    @Invoker("getParticleOffsets")
    Iterable<Vec3> subtle_effects$getParticleOffsets(BlockState state);
}
