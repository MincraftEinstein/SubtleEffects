package einstein.subtle_effects.mixin.client;

import com.ordana.immersive_weathering.blocks.soil_types.EarthenClayBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EarthenClayBlock.class)
public class EarthenClayBlockMixin {

    @Inject(method = "animateTick", at = @At(value = "HEAD"), cancellable = true)
    private void cancelDripsOverFluids(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (!level.getFluidState(pos.below()).isEmpty()) {
            ci.cancel();
        }
    }
}
