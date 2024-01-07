package einstein.ambient_sleep.mixin;

import einstein.ambient_sleep.AmbientSleep;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FurnaceBlock.class)
public class FurnaceBlockMixin {

    @Inject(method = "animateTick", at = @At("TAIL"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (state.getValue(FurnaceBlock.LIT)) {
            Direction direction = state.getValue(WallTorchBlock.FACING);
            Direction.Axis axis = direction.getAxis();
            AmbientSleep.spawnSparks(level, random, pos, new Vec3(0.5 + (0.6 * direction.getStepX()), random.nextDouble() * 6 / 16, 0.5 + (0.6 * direction.getStepZ())), new Vec3i(1, 1, 1), 3, axis == Direction.Axis.X ? 10 : 3, axis == Direction.Axis.Z ? 10 : 3, false);
        }
    }
}
