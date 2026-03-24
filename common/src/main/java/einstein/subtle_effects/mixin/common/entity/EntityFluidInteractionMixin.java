package einstein.subtle_effects.mixin.common.entity;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntityLandInFluidPayload;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.FluidDefinitionAccessor;
import einstein.subtle_effects.util.FluidLogicAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EntityFluidInteraction.class)
public class EntityFluidInteractionMixin {

    @Shadow
    @Final
    private Map<TagKey<Fluid>, EntityFluidInteraction.Tracker> trackerByFluid;

    @Inject(method = "update", at = @At("TAIL"))
    private void sendServerPlayerSplashes(Entity entity, boolean ignoreCurrent, CallbackInfo ci) {
        if (entity instanceof ServerPlayer serverPlayer) {
            this.trackerByFluid.forEach((fluidTag, tracker) -> {
                if (tracker.height > 0) {
                    FluidState fluidState = entity.level().getFluidState(entity.blockPosition());
                    if (!fluidState.isEmpty() && fluidState.tags().toList().contains(fluidTag)) {
                        Services.NETWORK.sendToClientsTracking(serverPlayer, (ServerLevel) entity.level(), entity.blockPosition(),
                                new ClientBoundEntityLandInFluidPayload(entity.getId(), entity.getY() + tracker.height,
                                        entity.getDeltaMovement().y(), fluidState.getType())
                        );
                    }
                }
            });
        }
    }

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityFluidInteraction$Tracker;accumulateCurrent(Lnet/minecraft/world/phys/Vec3;)V"))
    private void updateFluidPairHeight(Entity entity, boolean ignoreCurrent, CallbackInfo ci, @Local(name = "fluidState") FluidState fluidState, @Local(name = "tracker") EntityFluidInteraction.Tracker tracker) {
        if (entity.level().isClientSide()) {
            FluidDefinition fluidDefinition = ((FluidDefinitionAccessor) fluidState.getType()).subtleEffects$getFluidDefinition();
            if (fluidDefinition != null) {
                ((FluidLogicAccessor) entity).subtleEffects$getFluidDefinitionHeight().put(fluidDefinition, tracker.height);
            }
        }
    }
}
