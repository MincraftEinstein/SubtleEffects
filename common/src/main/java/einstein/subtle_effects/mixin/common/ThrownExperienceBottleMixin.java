package einstein.subtle_effects.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.networking.clientbound.ClientBoundXPBottleEffectsPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrownExperienceBottle.class)
public class ThrownExperienceBottleMixin {

    @WrapOperation(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;levelEvent(ILnet/minecraft/core/BlockPos;I)V"))
    private void cancelPotionParticles(ServerLevel level, int type, BlockPos pos, int data, Operation<Void> original) {
        Services.NETWORK.sendToClientsTracking(null, level, pos, new ClientBoundXPBottleEffectsPayload(pos), serverPlayer -> {
            serverPlayer.connection.send(new ClientboundLevelEventPacket(type, pos, data, false));
        });
    }
}
