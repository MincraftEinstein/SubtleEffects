package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.util.FrustumGetter;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static einstein.subtle_effects.init.ModConfigs.GENERAL;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @Shadow
    protected ClientLevel level;

    @Inject(method = "createParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"))
    private void modifyColor(ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfoReturnable<Particle> cir, @Local Particle particle) {
        if (Util.isBCWPPackLoaded() && Util.BIOME_COLORED_PARTICLES.contains(options.getType())) {
            Util.setColorFromHex(particle, level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor());
        }
    }

    @WrapWithCondition(method = "renderParticleType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V"))
    private static boolean shouldRenderParticle(Particle particle, VertexConsumer consumer, Camera camera, float partialTick) {
        return subtleEffects$shouldRenderParticle(particle, camera);
    }

    @WrapWithCondition(method = "renderCustomParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;renderCustom(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/Camera;F)V"))
    private static boolean shouldRenderCustomParticle(Particle particle, PoseStack poseStack, MultiBufferSource source, Camera camera, float partialTick) {
        return subtleEffects$shouldRenderParticle(particle, camera);
    }

    @Unique
    private static boolean subtleEffects$shouldRenderParticle(Particle particle, Camera camera) {
        Frustum frustum = ((FrustumGetter) Minecraft.getInstance().levelRenderer).subtleEffects$getCullingFrustum();
        if (frustum != null && frustum.isVisible(particle.getBoundingBox())) {
            ParticleAccessor accessor = ((ParticleAccessor) particle);

            if (GENERAL.cullParticlesInUnloadedChunks && !Util.isChunkLoaded(level, accessor.getX(), accessor.getZ())) {
                return false;
            }

            if (!GENERAL.cullParticlesWithNoAlpha || accessor.getAlpha() != 0) {
                int distance = GENERAL.particleRenderDistance * 16;
                return accessor.subtleEffects$wasForced() || camera.getPosition().distanceToSqr(accessor.getX(), accessor.getY(), accessor.getZ()) < distance * distance;
            }
        }
        return false;
    }
}
