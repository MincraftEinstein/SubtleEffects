package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @Shadow
    protected ClientLevel level;

    @Shadow
    @Final
    private Map<ResourceLocation, ParticleEngine.MutableSpriteSet> spriteSets;

    @Inject(method = "registerProviders", at = @At("TAIL"))
    private void registerProviders(CallbackInfo ci) {
        spriteSets.putAll(ModSpriteSets.REGISTERED);
    }

    @Inject(method = "createParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"))
    private void modifyColor(ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfoReturnable<Particle> cir, @Local Particle particle) {
        if (BCWPPackManager.isPackLoaded() && BCWPPackManager.BIOME_COLORED_PARTICLES.contains(options.getType())) {
            Util.setColorFromHex(particle, level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor());
        }
    }

    // TODO (ender) fix some of this later
  /*  @WrapWithCondition(method = "renderParticleType*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V"))
    private static boolean shouldRenderParticle(Particle particle, VertexConsumer consumer, Camera camera, float partialTick) {
        return subtleEffects$shouldRenderParticle(particle, camera);
    }

    @Unique
    private static boolean subtleEffects$shouldRenderParticle(Particle particle, Camera camera) {
        if (!GENERAL.enableParticleCulling) {
            return true;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Frustum frustum = ((FrustumGetter) minecraft.levelRenderer).subtleEffects$getCullingFrustum();
        if (frustum != null && frustum.isVisible(particle.getBoundingBox())) {
            ParticleAccessor accessor = ((ParticleAccessor) particle);

            if (GENERAL.cullParticlesInUnloadedChunks && !Util.isChunkLoaded(minecraft.level, accessor.getX(), accessor.getZ())) {
                return false;
            }

            int distance = GENERAL.particleRenderDistance.get() * 16;
            return accessor.subtleEffects$wasForced() || camera.getPosition().distanceToSqr(accessor.getX(), accessor.getY(), accessor.getZ()) < distance * distance;
        }
        return false;
    }*/
}
