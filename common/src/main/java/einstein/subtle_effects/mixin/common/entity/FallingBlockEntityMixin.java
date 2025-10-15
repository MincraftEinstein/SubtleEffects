package einstein.subtle_effects.mixin.common.entity;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.networking.clientbound.ClientBoundFallingBlockLandPayload;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.CommonUtil;
import einstein.subtle_effects.util.EntityAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
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
public class FallingBlockEntityMixin {

    @Unique
    private final FallingBlockEntity subtleEffects$me = (FallingBlockEntity) (Object) this;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;handlePortal()V"))
    private void tick(CallbackInfo ci) {
        einstein.subtle_effects.mixin.client.entity.EntityAccessor accessor = (einstein.subtle_effects.mixin.client.entity.EntityAccessor) subtleEffects$me;
        EntityAccessor entityAccessor = (EntityAccessor) subtleEffects$me;
        boolean isInWater = CommonUtil.isEntityInFluid(subtleEffects$me, FluidTags.WATER);
        boolean isInLava = CommonUtil.isEntityInFluid(subtleEffects$me, FluidTags.LAVA);

        if (subtleEffects$me.level().isClientSide()) {
            if (isInWater && !subtleEffects$me.isInWater()) {
                accessor.doWaterSplashingEffects();
            }

            ParticleSpawnUtil.spawnLavaSplash(subtleEffects$me, isInLava, false, entityAccessor.subtleEffects$wasTouchingLava());
        }
        accessor.subtleEffects$setTouchingWater(isInWater);
        entityAccessor.subtleEffects$setTouchingLava(isInLava);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Fallable;onLand(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/item/FallingBlockEntity;)V"))
    private void onLand(CallbackInfo ci, @Local BlockPos pos) {
        Services.NETWORK.sendToClientsTracking((ServerLevel) subtleEffects$me.level(), pos, new ClientBoundFallingBlockLandPayload(subtleEffects$me.getBlockState(), pos, subtleEffects$me.isInWater()));
    }
}
