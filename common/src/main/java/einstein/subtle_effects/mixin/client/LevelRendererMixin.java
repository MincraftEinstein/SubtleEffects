package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import einstein.subtle_effects.util.FrustumGetter;
import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin implements FrustumGetter {

    @Shadow
    private Frustum cullingFrustum;

    @ModifyReturnValue(method = "addParticleInternal(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At(value = "RETURN", ordinal = 0))
    private Particle spawnForcedParticle(Particle particle) {
        if (particle != null) {
            ((ParticleAccessor) particle).subtleEffects$force();
        }
        return particle;
    }

    @Override
    public Frustum subtleEffects$getCullingFrustum() {
        return cullingFrustum;
    }
}
