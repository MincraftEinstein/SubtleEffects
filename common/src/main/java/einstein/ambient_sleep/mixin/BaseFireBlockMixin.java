package einstein.ambient_sleep.mixin;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin {

    @Shadow
    protected abstract boolean canBurn(BlockState blockState);

    @Inject(method = "animateTick", at = @At("TAIL"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (!ModConfigs.INSTANCE.enableFireSparks.get()) {
            return;
        }

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        if (!canBurn(belowState) && !belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
            if (ambientSleep$canBurn(level, pos.west())) {
                ambientSleep$spawnSparks(level, random, state, pos, 0, random.nextDouble());
            }

            if (ambientSleep$canBurn(level, pos.east())) {
                ambientSleep$spawnSparks(level, random, state, pos, 1, random.nextDouble());
            }

            if (ambientSleep$canBurn(level, pos.north())) {
                ambientSleep$spawnSparks(level, random, state, pos, random.nextDouble(), 0);
            }

            if (ambientSleep$canBurn(level, pos.south())) {
                ambientSleep$spawnSparks(level, random, state, pos, random.nextDouble(), 1);
            }
            return;
        }
        ambientSleep$spawnSparks(level, random, state, pos, random.nextDouble(), random.nextDouble());
    }

    @Unique
    private boolean ambientSleep$canBurn(Level level, BlockPos pos) {
        return canBurn(level.getBlockState(pos));
    }

    @Unique
    private static void ambientSleep$spawnSparks(Level level, RandomSource random, BlockState state, BlockPos pos, double xOffset, double zOffset) {
        Util.spawnSparks(level, random, pos, new Vec3(xOffset, random.nextDouble(), zOffset), new Vec3i(3, 5, 3), 10, 10, state.is(Blocks.SOUL_FIRE), true);
    }
}
