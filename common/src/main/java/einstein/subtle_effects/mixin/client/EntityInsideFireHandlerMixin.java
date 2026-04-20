package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.init.ModConfigs;
import fuzs.dyedflames.handler.EntityInsideFireHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded(CompatHelper.DYED_FLAMES_MOD_ID)
@Mixin(value = EntityInsideFireHandler.class, remap = false)
public class EntityInsideFireHandlerMixin {

    @ModifyExpressionValue(method = "emitFlameParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;displayFireAnimation()Z"))
    private static boolean cancelFireParticles(boolean original) {
        return original && !ModConfigs.ENTITIES.burning.overrideDyedFlamesEffects;
    }
}
