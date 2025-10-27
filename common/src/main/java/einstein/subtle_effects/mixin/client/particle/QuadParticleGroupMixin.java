package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.QuadParticleGroup;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.init.ModConfigs.GENERAL;

@Mixin(QuadParticleGroup.class)
public class QuadParticleGroupMixin {

    @ModifyExpressionValue(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/culling/Frustum;pointInFrustum(DDD)Z"))
    boolean enhancedFrustumCull(boolean original, Frustum frustum, Camera camera, float partialTick, @Local SingleQuadParticle singlequadparticle) {
        if (!original && GENERAL.enableParticleCulling) {
            return frustum != null && frustum.isVisible(singlequadparticle.getBoundingBox());
        }
        return original;
    }
}
