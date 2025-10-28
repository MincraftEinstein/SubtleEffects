package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @Shadow
    protected ClientLevel level;

    @Inject(method = "createParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"))
    private void modifyColor(ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfoReturnable<Particle> cir, @Local Particle particle) {
        if (particle instanceof SingleQuadParticle sqParticle) {
            ParticleType<?> type = options.getType();
            if (ModConfigs.GENERAL.particleCullingBlocklist.contains(type)) {
                ((ParticleAccessor) particle).subtleEffects$ignoresCulling();
            }

            if (BCWPPackManager.isPackLoaded() && BCWPPackManager.BIOME_COLORED_PARTICLES.contains(type)) {
                Util.setColorFromHex(sqParticle, level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor());
            }
        }
    }
}
