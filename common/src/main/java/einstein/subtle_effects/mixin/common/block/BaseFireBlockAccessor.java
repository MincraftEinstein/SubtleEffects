package einstein.subtle_effects.mixin.common.block;

import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BaseFireBlock.class)
public interface BaseFireBlockAccessor {

    @Invoker("canBurn")
    boolean subtle_effects$canBurn(BlockState state);
}
