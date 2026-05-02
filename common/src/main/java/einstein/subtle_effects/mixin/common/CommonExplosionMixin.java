package einstein.subtle_effects.mixin.common;

import einstein.subtle_effects.networking.clientbound.ClientBoundChargedCreeperExplosionPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Explosion.class)
public class CommonExplosionMixin {

    @Shadow
    @Final
    @Nullable
    private Entity source;

    @Shadow
    @Final
    private double z;

    @Shadow
    @Final
    private double y;

    @Shadow
    @Final
    private double x;

    @Shadow
    @Final
    private float radius;

    @Shadow
    @Final
    private Level level;

    @Inject(method = "finalizeExplosion", at = @At("HEAD"))
    private void spawnChargedCreeperParticles(boolean spawnParticles, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel) {
            if (source instanceof Creeper creeper && creeper.isPowered()) {
                Services.NETWORK.sendToClientsTracking(serverLevel, BlockPos.containing(x, y, z), new ClientBoundChargedCreeperExplosionPayload(x, y, z, radius));
            }
        }
    }
}
