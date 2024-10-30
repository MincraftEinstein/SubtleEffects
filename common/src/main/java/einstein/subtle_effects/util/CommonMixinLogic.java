package einstein.subtle_effects.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.particles.SimpleParticleType;

import static einstein.subtle_effects.init.ModConfigs.GENERAL;

public class CommonMixinLogic {

    public static SimpleParticleType replaceBucketEvaporationSmoke(Operation<SimpleParticleType> original) {
        if (ModConfigs.ITEMS.waterEvaporateFromBucketSteam) {
            return ModParticles.STEAM.get();
        }
        return original.call();
    }

    public static boolean shouldRenderParticle(Particle particle, Camera camera) {
        Frustum frustum = ((FrustumGetter) Minecraft.getInstance().levelRenderer).subtleEffects$getCullingFrustum();
        if (frustum != null && frustum.isVisible(particle.getBoundingBox())) {
            ParticleAccessor accessor = ((ParticleAccessor) particle);
            if (!GENERAL.cullParticlesWithNoAlpha || accessor.getAlpha() != 0) {
                int distance = GENERAL.particleRenderDistance * 16;
                return accessor.subtleEffects$wasForced() || camera.getPosition().distanceToSqr(accessor.getX(), accessor.getY(), accessor.getZ()) < distance * distance;
            }
        }
        return false;
    }
}
