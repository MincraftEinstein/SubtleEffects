package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @ModifyReturnValue(method = "getNightVisionScale", at = @At("RETURN"))
    private static float getNightVisionScale(float original, @Local MobEffectInstance instance) {
        if (ModConfigs.GENERAL.nightVisionFading) {
            int duration = instance.getDuration();
            return !instance.endsWithin(100) ? 1 : duration * 0.01F;
        }
        return original;
    }
}
