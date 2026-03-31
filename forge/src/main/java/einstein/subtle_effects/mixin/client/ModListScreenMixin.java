package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.Util;
import net.minecraftforge.client.gui.ModListScreen;
import net.minecraftforge.forgespi.language.IModInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModListScreen.class, remap = false)
public class ModListScreenMixin {

    @Inject(method = "lambda$reloadMods$9", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/forgespi/language/IModInfo;getDisplayName()Ljava/lang/String;"))
    private void setIsSubtleEffects(IModInfo mi, CallbackInfoReturnable<Boolean> cir, @Share("isSubtleEffects") LocalBooleanRef isSubtleEffects) {
        isSubtleEffects.set(mi.getModId().equals(SubtleEffects.MOD_ID));
    }

    @WrapOperation(method = "lambda$reloadMods$9", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"))
    private boolean filterSouthEast(String modDisplayName, CharSequence searchQuery, Operation<Boolean> original, @Share("isSubtleEffects") LocalBooleanRef isSubtleEffects) {
        return original.call(modDisplayName, searchQuery) || (isSubtleEffects.get() && Util.isSouthEast(searchQuery));
    }
}
