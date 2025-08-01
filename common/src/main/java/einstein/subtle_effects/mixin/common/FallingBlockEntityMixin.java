package einstein.subtle_effects.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.networking.clientbound.ClientBoundFallingBlockLandPayload;
import einstein.subtle_effects.networking.clientbound.ClientBoundFallingBlockTickPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin {

    @Unique
    private final FallingBlockEntity subtleEffects$me = (FallingBlockEntity) (Object) this;
    @Unique
    private double subtleEffects$oldFallDistance;

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Fallable;onLand(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/item/FallingBlockEntity;)V"))
    private void onLand(Fallable block, Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock, Operation<Void> original) {
        Services.NETWORK.sendToClientsTracking((ServerLevel) level, pos, new ClientBoundFallingBlockLandPayload(state, pos));
        original.call(block, level, pos, state, replaceableState, fallingBlock);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;onGround()Z"))
    private void tick(CallbackInfo ci, @Local(ordinal = 1) boolean isInWater) {
        if (!subtleEffects$me.onGround() && !isInWater && !subtleEffects$me.isNoGravity()) {
            double fallDistance = subtleEffects$me.fallDistance;

            if (fallDistance != subtleEffects$oldFallDistance) {
                Services.NETWORK.sendToClientsTracking((ServerLevel) subtleEffects$me.level(), subtleEffects$me.blockPosition(), new ClientBoundFallingBlockTickPayload(subtleEffects$me.getId(), fallDistance));
                subtleEffects$oldFallDistance = fallDistance;
            }
        }
    }
}
