package einstein.ambient_sleep.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.util.FrustumGetter;
import einstein.ambient_sleep.util.ParticleAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {

    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V"))
    private void renderParticle(Particle particle, VertexConsumer consumer, Camera camera, float partialTick) {
        Frustum frustum = ((FrustumGetter) Minecraft.getInstance().levelRenderer).ambientSleep$getCullingFrustum();
        if (frustum != null && frustum.isVisible(particle.getBoundingBox())) {
            ParticleAccessor accessor = ((ParticleAccessor) particle);
            int distance = ModConfigs.INSTANCE.particleRenderDistance.get() * 16;
            if (accessor.ambientSleep$wasForced() || camera.getPosition().distanceToSqr(accessor.getX(), accessor.getY(), accessor.getZ()) < distance * distance) {
                particle.render(consumer, camera, partialTick);
            }
        }
    }
}
