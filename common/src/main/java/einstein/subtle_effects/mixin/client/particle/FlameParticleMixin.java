package einstein.subtle_effects.mixin.client.particle;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.particle.FlameParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlameParticle.class)
public class FlameParticleMixin {

    @Inject(method = "getLightCoords", at = @At("HEAD"), cancellable = true)
    private void getLightCoords(float partialTick, CallbackInfoReturnable<Integer> cir) {
        if (ModConfigs.GENERAL.staticFlameBrightness) {
            cir.setReturnValue(Util.PARTICLE_LIGHT_COLOR);
        }
    }
}
