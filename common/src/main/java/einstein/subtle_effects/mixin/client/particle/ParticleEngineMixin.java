package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.FrustumGetter;
import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @WrapWithCondition(method = "render*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V"))
    private boolean shouldRenderParticle(Particle particle, VertexConsumer consumer, Camera camera, float partialTick) {
        Frustum frustum = ((FrustumGetter) Minecraft.getInstance().levelRenderer).subtleEffects$getCullingFrustum();
        if (frustum != null && frustum.isVisible(particle.getBoundingBox())) {
            ParticleAccessor accessor = ((ParticleAccessor) particle);
            if (accessor.getAlpha() != 0) {
                int distance = ModConfigs.GENERAL.particleRenderDistance * 16;
                return accessor.subtleEffects$wasForced() || camera.getPosition().distanceToSqr(accessor.getX(), accessor.getY(), accessor.getZ()) < distance * distance;
            }
        }
        return false;
    }
}
