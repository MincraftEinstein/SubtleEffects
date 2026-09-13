package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.configs.cache.ParticleCullingCache;
import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.util.FrustumGetter;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @Shadow
    protected ClientLevel level;

    @Unique
    private Frustum subtleEffects$frustum;

    @Inject(method = "render*", at = @At("HEAD"))
    private void cacheFrustumForRenderPass(CallbackInfo ci) {
        subtleEffects$frustum = ((FrustumGetter) Minecraft.getInstance().levelRenderer).subtleEffects$getCullingFrustum();
    }

    @Inject(method = "createParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"))
    private void modifyParticle(ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfoReturnable<Particle> cir, @Local Particle particle) {
        ParticleType<?> type = options.getType();
        if (ParticleCullingCache.cullingBlocklist.contains(type)) {
            ((ParticleAccessor) particle).subtleEffects$ignoresCulling();
        }

        if (BCWPPackManager.isPackLoaded() && BCWPPackManager.BIOME_COLORED_PARTICLES.contains(type)) {
            Util.setColorFromHex(particle, BiomeColors.getAverageWaterColor(level, BlockPos.containing(x, y, z)));
        }
    }

    @WrapWithCondition(method = "render*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V"))
    private boolean shouldRenderParticle(Particle particle, VertexConsumer consumer, Camera camera, float partialTick) {
        if (!ParticleCullingCache.enabled) {
            return true;
        }

        if (particle.getRenderType() == ParticleRenderType.CUSTOM) {
            return true;
        }

        ParticleAccessor accessor = (ParticleAccessor) particle;
        if (accessor.subtleEffects$shouldIgnoreCulling()) {
            return true;
        }

        Frustum frustum = subtleEffects$frustum;
        if (frustum != null && frustum.isVisible(particle.getBoundingBox())) {
            if (ParticleCullingCache.cullInUnloadedChunks && !Util.isChunkLoaded(level, accessor.getX(), accessor.getZ())) {
                return false;
            }

            return accessor.subtleEffects$wasForced()
                    || camera.getPosition().distanceToSqr(accessor.getX(), accessor.getY(), accessor.getZ()) < ParticleCullingCache.renderDistanceSquared;
        }
        return false;
    }
}
