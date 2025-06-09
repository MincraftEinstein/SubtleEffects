package einstein.subtle_effects.mixin.client.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractCauldronBlock.class)
public interface AbstractCauldronBlockAccessor {

    @Invoker("getContentHeight")
    double getFillHeight(BlockState state);

    @Invoker("isEntityInsideContent")
    boolean isEntityInside(BlockState state, BlockPos pos, Entity entity);
}
