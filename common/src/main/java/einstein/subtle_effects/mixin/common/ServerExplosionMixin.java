package einstein.subtle_effects.mixin.common;

import einstein.subtle_effects.networking.PayloadSender;
import einstein.subtle_effects.networking.clientbound.ClientBoundChargedCreeperExplosionPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerExplosion.class)
public abstract class ServerExplosionMixin implements Explosion {

    @Inject(method = "explode", at = @At("HEAD"))
    private void spawnChargedCreeperParticles(CallbackInfoReturnable<Integer> cir) {
        if (getDirectSourceEntity() instanceof Creeper creeper && creeper.isPowered()) {
            Vec3 center = center();
            PayloadSender.sendToClientsTracking(level(), BlockPos.containing(center), new ClientBoundChargedCreeperExplosionPayload(center, radius()));
        }
    }
}
