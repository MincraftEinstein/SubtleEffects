package einstein.subtle_effects.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.networking.clientbound.ClientBoundExplosionPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @Inject(method = "explode", at = @At(value = "TAIL"))
    private void explode(Entity source, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean fire, Level.ExplosionInteraction explosionInteraction, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, Holder<SoundEvent> explosionSound, CallbackInfo ci, @Local ServerExplosion explosion) {
        Services.NETWORK.sendToClientsTracking((ServerLevel) (Object) this, BlockPos.containing(explosion.center()), new ClientBoundExplosionPayload(explosion.center(), explosion.radius()));
    }
}
