package einstein.subtle_effects.mixin.common.entity;

import einstein.subtle_effects.networking.clientbound.ClientBoundEntityLandInFluidPayload;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.CommonEntityAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class CommonEntityMixin implements CommonEntityAccessor {

    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @Unique
    private boolean subtleEffects$wasTouchingLava = false;

    @Inject(method = "doWaterSplashEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;floor(D)I"))
    private void doWaterSplash(CallbackInfo ci) {
        if (subtleEffects$me instanceof ServerPlayer player) {
            Services.NETWORK.sendToClientsTracking(player, (ServerLevel) subtleEffects$me.level(), subtleEffects$me.blockPosition(),
                    new ClientBoundEntityLandInFluidPayload(subtleEffects$me.getId(),
                            subtleEffects$me.getY() + subtleEffects$me.getFluidHeight(FluidTags.WATER),
                            subtleEffects$me.getDeltaMovement().y(), false
                    )
            );
        }
    }

    @Override
    public boolean subtleEffects$wasTouchingLava() {
        return subtleEffects$wasTouchingLava;
    }

    @Override
    public void subtleEffects$setTouchingLava(boolean touchingLava) {
        subtleEffects$wasTouchingLava = touchingLava;
    }
}
