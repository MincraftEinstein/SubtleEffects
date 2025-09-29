package einstein.subtle_effects.mixin.client.block;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.MathUtil;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BarrelBlock.class)
public class BarrelBlockMixin {

    @Inject(method = "useWithoutItem", at = @At(value = "FIELD", target = "Lnet/minecraft/world/InteractionResult;SUCCESS:Lnet/minecraft/world/InteractionResult$Success;"))
    private void useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!ModConfigs.BLOCKS.openingBarrelsSpawnsBubbles) {
            return;
        }

        Direction direction = state.getValue(BarrelBlock.FACING);
        BlockPos relativePos = pos.relative(direction);

        if (direction != Direction.DOWN && level.getFluidState(relativePos).is(FluidTags.WATER) && !level.getBlockState(relativePos).isSolidRender()) {
            RandomSource random = level.getRandom();

            for (int i = 0; i < 10; i++) {
                ParticleSpawnUtil.spawnParticlesOnSide(ParticleTypes.BUBBLE_COLUMN_UP, 0.0625F, direction, level, pos, level.getRandom(),
                        MathUtil.nextDouble(random, 0.3) * direction.getStepX(),
                        MathUtil.nextDouble(random, 0.3) * direction.getStepY(),
                        MathUtil.nextDouble(random, 0.3) * direction.getStepZ()
                );
            }
        }
    }
}
