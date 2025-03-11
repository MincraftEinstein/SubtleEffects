package einstein.subtle_effects.mixin.client.particle.bubbles;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.util.BubbleSetter;
import net.minecraft.client.particle.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BubbleParticle.Provider.class)
public class BubbleParticleProviderMixin {

    @Shadow
    @Final
    private SpriteSet sprite;

    @ModifyReturnValue(method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("RETURN"))
    private Particle createParticle(Particle particle) {
        if (particle != null && BCWPPackManager.isPackLoaded()) {
            ((BubbleSetter) particle).subtleEffects$setupBubble(sprite, true);
        }
        return particle;
    }
}
