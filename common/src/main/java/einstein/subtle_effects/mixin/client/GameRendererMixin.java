package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.init.ModConfigs.GENERAL;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @ModifyReturnValue(method = "getNightVisionScale", at = @At("RETURN"))
    private static float getNightVisionScale(float original, @Local MobEffectInstance instance) {
        if (GENERAL.nightVisionFading) {
            float brightness = 1;
            int duration = instance.getDuration();
            return !instance.endsWithin(GENERAL.nightVisionFadingTime.get()) ? brightness : duration * (brightness / GENERAL.nightVisionFadingTime.get());
        }
        return original;
    }
}
