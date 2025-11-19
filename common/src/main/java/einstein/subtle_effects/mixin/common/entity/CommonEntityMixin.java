package einstein.subtle_effects.mixin.common.entity;

import einstein.subtle_effects.networking.clientbound.ClientBoundEntityLandInFluidPayload;
import einstein.subtle_effects.platform.Services;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class CommonEntityMixin {

    @Shadow
    protected boolean firstTick;

    @Shadow
    protected Object2DoubleMap<TagKey<Fluid>> fluidHeight;

    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @Inject(method = "updateInWaterStateAndDoFluidPushing", at = @At("TAIL"))
    private void updateInWaterStateAndDoFluidPushing(CallbackInfoReturnable<Boolean> cir) {
        if (subtleEffects$me instanceof ServerPlayer serverPlayer && !firstTick) {
            fluidHeight.forEach((fluidTag, height) -> {
                if (height > 0) {
                    FluidState fluidState = subtleEffects$me.level().getFluidState(subtleEffects$me.blockPosition());
                    if (!fluidState.isEmpty() && fluidState.getTags().toList().contains(fluidTag)) {
                        Services.NETWORK.sendToClientsTracking(serverPlayer, (ServerLevel) subtleEffects$me.level(), subtleEffects$me.blockPosition(),
                                new ClientBoundEntityLandInFluidPayload(subtleEffects$me.getId(), subtleEffects$me.getY() + height,
                                        subtleEffects$me.getDeltaMovement().y(), fluidState.getType())
                        );
                    }
                }
            });
        }
    }
}
