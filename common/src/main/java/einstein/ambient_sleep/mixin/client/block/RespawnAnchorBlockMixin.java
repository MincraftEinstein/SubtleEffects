package einstein.ambient_sleep.mixin.client.block;

import einstein.ambient_sleep.util.ParticleSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RespawnAnchorBlock.class)
public class RespawnAnchorBlockMixin {

    @Inject(method = "animateTick", at = @At("HEAD"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (random.nextInt(5) == 0) {
            Direction direction = Direction.getRandom(random);

            if (direction != Direction.UP) {
                BlockPos relativePos = pos.relative(direction);
                BlockState relativeState = level.getBlockState(relativePos);

                if (!state.canOcclude() || !relativeState.isFaceSturdy(level, relativePos, direction.getOpposite())) {
                    ParticleSpawnUtil.spawnParticlesOnSide(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, 0.1F, direction, level, pos, random, 0, 0, 0);
                }
            }
        }
    }
}
