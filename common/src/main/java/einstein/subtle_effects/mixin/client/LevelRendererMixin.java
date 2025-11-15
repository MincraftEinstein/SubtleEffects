package einstein.subtle_effects.mixin.client;

import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    // TODO
//    @ModifyReturnValue(method = "addParticleInternal(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At(value = "RETURN", ordinal = 0))
//    private Particle spawnForcedParticle(Particle particle) {
//        if (particle != null) {
//            ((ParticleAccessor) particle).subtleEffects$force();
//        }
//        return particle;
//    }
}
