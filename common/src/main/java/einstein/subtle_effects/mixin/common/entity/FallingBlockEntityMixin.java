package einstein.subtle_effects.mixin.common.entity;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.networking.clientbound.ClientBoundFallingBlockLandPayload;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.FluidLogicAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.Consumers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

    @Shadow
    public abstract BlockState getBlockState();

    public FallingBlockEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;handlePortal()V"))
    private void tick(CallbackInfo ci) {
        FluidLogicAccessor.clientUpdateInWaterStateAndDoFluidPushing(this);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Fallable;onLand(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/item/FallingBlockEntity;)V"))
    private void onLand(CallbackInfo ci, @Local BlockPos pos) {
        Services.NETWORK.sendToClientsTracking((ServerLevel) level(), pos, new ClientBoundFallingBlockLandPayload(getBlockState(), pos, isInWater()));
    }
}
