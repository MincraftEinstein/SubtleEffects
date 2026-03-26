package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.Util;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.ModListScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModListScreen.class)
public class ModListScreenMixin {

    @Inject(method = "lambda$reloadMods$10", at = @At(value = "INVOKE", target = "Lnet/neoforged/fml/ModContainer;getModInfo()Lnet/neoforged/neoforgespi/language/IModInfo;"))
    private void setIsSubtleEffects(ModContainer modContainer, CallbackInfoReturnable<Boolean> cir, @Share("isSubtleEffects") LocalBooleanRef isSubtleEffects) {
        isSubtleEffects.set(modContainer.getModId().equals(SubtleEffects.MOD_ID));
    }

    @WrapOperation(method = "lambda$reloadMods$10", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"))
    private boolean filterSouthEast(String modDisplayName, CharSequence searchQuery, Operation<Boolean> original, @Share("isSubtleEffects") LocalBooleanRef isSubtleEffects) {
        return original.call(modDisplayName, searchQuery) || (isSubtleEffects.get() && Util.isSouthEast(searchQuery));
    }
}
