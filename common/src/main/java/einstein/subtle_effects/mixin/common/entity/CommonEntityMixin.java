package einstein.subtle_effects.mixin.common.entity;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntityLandInFluidPayload;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class CommonEntityMixin {

    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @Inject(method = "doWaterSplashEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;floor(D)I"), cancellable = true)
    private void doWaterSplash(CallbackInfo ci) {
        Level level = subtleEffects$me.level();
        double yVelocity = subtleEffects$me.getDeltaMovement().y();
        double y = subtleEffects$me.getY();

        if (subtleEffects$me instanceof ServerPlayer player) {
            Services.NETWORK.sendToClientsTracking(player, (ServerLevel) level, subtleEffects$me.blockPosition(),
                    new ClientBoundEntityLandInFluidPayload(subtleEffects$me.getId(), y + subtleEffects$me.getFluidHeight(FluidTags.WATER), yVelocity, false)
            );
            return;
        }

        if (ParticleSpawnUtil.spawnSplashEffects(subtleEffects$me, level, ModParticles.WATER_SPLASH_EMITTER.get(), FluidTags.WATER)) {
            ci.cancel();
        }
    }
}
