package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @WrapOperation(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean destroyBlock(Level level, BlockPos pos, BlockState newState, int flags, Operation<Boolean> original, @Local BlockState state) {
        boolean success = original.call(level, pos, newState, flags);

        if (success && ModConfigs.BLOCKS.underwaterBlockBreakBubbles) {
            boolean nearWater = false;
            for (Direction direction : Direction.values()) {
                BlockPos relativePos = pos.relative(direction);
                FluidState relativeFluidState = level.getFluidState(relativePos);
                BlockState relativeState = level.getBlockState(pos);

                if (relativeFluidState.is(FluidTags.WATER) && relativeFluidState.getAmount() >= 7 && !Block.isFaceFull(relativeState.getCollisionShape(level, pos), direction.getOpposite())) {
                    nearWater = true;
                    break;
                }
            }

            if (nearWater) {
                TickerManager.schedule(7, () -> {
                    FluidState newFluidState = level.getFluidState(pos);
                    if (newFluidState.is(FluidTags.WATER) && newFluidState.getAmount() >= 7) {
                        ParticleSpawnUtil.spawnParticlesAroundShape(ParticleTypes.BUBBLE, level, pos, state,
                                direction -> {
                                    BlockPos relativePos = pos.relative(direction);
                                    return Block.isFaceFull(level.getBlockState(relativePos).getCollisionShape(level, relativePos), direction.getOpposite());
                                },
                                3, () -> new Vec3(0, 0, 0), 0
                        );
                    }
                });
            }
        }

        return success;
    }
}
