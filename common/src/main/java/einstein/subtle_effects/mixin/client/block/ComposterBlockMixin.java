package einstein.subtle_effects.mixin.client.block;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
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
        if (ModConfigs.INSTANCE.compostingParticles.get()) {
            RandomSource random = level.getRandom();
            BlockState state = level.getBlockState(pos);

            for (int i = 0; i < 10; i++) {
                level.addParticle(ModParticles.COMPOST.get(),
                        pos.getX() + 0.5 + MathUtil.nextNonAbsDouble(random, 0.3),
                        pos.getY() + 0.1875 + (0.125 * state.getValue(ComposterBlock.LEVEL)),
                        pos.getZ() + 0.5 + MathUtil.nextNonAbsDouble(random, 0.3),
                        0, 0, 0
                );
            }
        }
    }
}
