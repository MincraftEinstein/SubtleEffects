package einstein.ambient_sleep.mixin.client.block;

import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {

    @Inject(method = "handleFill", at = @At("TAIL"))
    private static void handleFill(Level level, BlockPos pos, boolean success, CallbackInfo ci) {
        BlockState state = level.getBlockState(pos);

        for (int i = 0; i < 10; i++) {
            level.addParticle(ModParticles.COMPOST.get(),
                    pos.getX() + 0.5 + MathUtil.nextFloat(30) * MathUtil.nextSign(),
                    pos.getY() + 0.1875 + (0.125 * state.getValue(ComposterBlock.LEVEL)),
                    pos.getZ() + 0.5 + MathUtil.nextFloat(30) * MathUtil.nextSign(),
                    0, 0, 0
            );
        }
    }
}
