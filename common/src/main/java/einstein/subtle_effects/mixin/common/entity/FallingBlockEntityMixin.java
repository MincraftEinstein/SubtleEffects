package einstein.subtle_effects.mixin.common.entity;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.networking.clientbound.ClientBoundFallingBlockLandPayload;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.CommonUtil;
import einstein.subtle_effects.util.FallingBlockAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin implements FallingBlockAccessor {

    @Unique
    private final FallingBlockEntity subtleEffects$me = (FallingBlockEntity) (Object) this;

    @Unique
    private boolean subtleEffects$isInWater;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;handlePortal()V"))
    private void tick(CallbackInfo ci) {
        subtleEffects$isInWater = CommonUtil.isEntityInFluid(subtleEffects$me, FluidTags.WATER);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Fallable;onLand(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/item/FallingBlockEntity;)V"))
    private void onLand(CallbackInfo ci, @Local BlockPos pos) {
        Services.NETWORK.sendToClientsTracking((ServerLevel) subtleEffects$me.level(), pos, new ClientBoundFallingBlockLandPayload(subtleEffects$me.getBlockState(), pos, subtleEffects$isInWater));
    }

    @Override
    public boolean subtleEffects$isInWater() {
        return subtleEffects$isInWater;
    }
}
