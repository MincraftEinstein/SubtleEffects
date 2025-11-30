package einstein.subtle_effects.mixin.common.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.networking.clientbound.ClientBoundCopperGolemPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.golem.CopperGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CopperGolem.class)
public class CopperGolemMixin {

    @WrapOperation(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;levelEvent(Lnet/minecraft/world/entity/Entity;ILnet/minecraft/core/BlockPos;I)V"))
    private void cancelGolemInteractParticles(Level level, Entity entity, int event, BlockPos pos, int data, Operation<Void> original) {
        if (level instanceof ServerLevel serverLevel) {
            ClientBoundCopperGolemPayload.Action action = switch (event) {
                case LevelEvent.PARTICLES_SCRAPE -> ClientBoundCopperGolemPayload.Action.SCRAPE;
                case LevelEvent.PARTICLES_WAX_OFF -> ClientBoundCopperGolemPayload.Action.WAX_OFF;
                case LevelEvent.PARTICLES_AND_SOUND_WAX_ON -> ClientBoundCopperGolemPayload.Action.WAX_ON;
                default -> null;
            };

            if (action != null) {
                Services.NETWORK.sendToClientsTracking(null, serverLevel, pos, new ClientBoundCopperGolemPayload(entity.getId(), action), serverPlayer ->
                        serverPlayer.connection.send(new ClientboundLevelEventPacket(event, pos, data, false))
                );
                return;
            }
        }
        original.call(level, entity, event, pos, data);
    }
}
