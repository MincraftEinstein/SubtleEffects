package einstein.subtle_effects.mixin.common;

import einstein.subtle_effects.networking.clientbound.ClientBoundEntityFellPacket;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MaceItem.class)
public class MaceItemMixin {

    @Inject(method = "knockback", at = @At("HEAD"))
    private static void spawnDustClouds(Level level, Entity attacker, Entity target, CallbackInfo ci) {
        ParticleSpawnUtil.spawnFallDustClouds(attacker, 10, 10, ClientBoundEntityFellPacket.TypeConfig.MACE);
    }
}
