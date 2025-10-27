package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SingleQuadParticle;
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
        if (particle instanceof SingleQuadParticle sqParticle) {
            if (BCWPPackManager.isPackLoaded() && BCWPPackManager.BIOME_COLORED_PARTICLES.contains(options.getType())) {
                Util.setColorFromHex(sqParticle, level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor());
            }
        }
    }

    @Unique
    private static boolean subtleEffects$shouldRenderParticle(Particle particle, Camera camera) {
        Minecraft minecraft = Minecraft.getInstance();
        ParticleAccessor accessor = ((ParticleAccessor) particle);
        if (GENERAL.cullParticlesInUnloadedChunks && !Util.isChunkLoaded(minecraft.level, accessor.getX(), accessor.getZ())) {
            return false;
        }

        int distance = GENERAL.particleRenderDistance.get() * 16;
        return accessor.subtleEffects$wasForced() || camera.getPosition().distanceToSqr(accessor.getX(), accessor.getY(), accessor.getZ()) < distance * distance;
    }
}
