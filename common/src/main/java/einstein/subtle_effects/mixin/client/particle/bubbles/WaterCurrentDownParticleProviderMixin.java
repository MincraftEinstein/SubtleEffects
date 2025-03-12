package einstein.subtle_effects.mixin.client.particle.bubbles;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.util.BubbleSetter;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.WaterCurrentDownParticle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WaterCurrentDownParticle.Provider.class)
public class WaterCurrentDownParticleProviderMixin {

    @Shadow
    @Final
    private SpriteSet sprite;

    @ModifyReturnValue(method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("RETURN"))
    private Particle createParticle(Particle particle) {
        if (particle != null && BCWPPackManager.isPackLoaded()) {
            ((BubbleSetter) particle).subtleEffects$setupBubble(sprite, false);
        }
        return particle;
    }
}
