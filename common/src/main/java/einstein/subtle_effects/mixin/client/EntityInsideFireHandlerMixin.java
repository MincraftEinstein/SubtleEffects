package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.subtle_effects.init.ModConfigs;
import fuzs.dyedflames.handler.EntityInsideFireHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EntityInsideFireHandler.class, remap = false)
public class EntityInsideFireHandlerMixin {

    @ModifyExpressionValue(method = "onEndEntityTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;displayFireAnimation()Z"))
    private static boolean cancel(boolean original) {
        return original && !ModConfigs.ENTITIES.burning.overrideDyedFlamesEffects;
    }
}
