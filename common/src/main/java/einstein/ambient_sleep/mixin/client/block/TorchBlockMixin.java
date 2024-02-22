package einstein.ambient_sleep.mixin.client.block;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TorchBlock.class)
public class TorchBlockMixin {

    @Inject(method = "animateTick", at = @At("TAIL"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (!ModConfigs.INSTANCE.torchSparks.get()) {
            return;
        }

        Util.spawnSparks(level, random, pos, new Vec3(0.5, 0.5, 0.5), new Vec3i(1, 1, 1), 2, -6, state.is(Blocks.SOUL_TORCH), false);
    }
}
