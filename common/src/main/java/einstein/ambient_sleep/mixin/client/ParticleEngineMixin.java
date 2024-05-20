package einstein.ambient_sleep.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.util.FrustumGetter;
import einstein.ambient_sleep.util.ParticleAccessor;
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
        Frustum frustum = ((FrustumGetter) Minecraft.getInstance().levelRenderer).ambientSleep$getCullingFrustum();
        if (frustum != null && frustum.isVisible(particle.getBoundingBox())) {
            ParticleAccessor accessor = ((ParticleAccessor) particle);
            int distance = ModConfigs.INSTANCE.particleRenderDistance.get() * 16;
            return accessor.ambientSleep$wasForced() || camera.getPosition().distanceToSqr(accessor.getX(), accessor.getY(), accessor.getZ()) < distance * distance;
        }
        return false;
    }
}
