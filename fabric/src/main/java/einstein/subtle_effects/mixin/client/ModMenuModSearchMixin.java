package einstein.subtle_effects.mixin.client;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import com.terraformersmc.modmenu.config.ModMenuConfig;
import com.terraformersmc.modmenu.gui.ModsScreen;
import com.terraformersmc.modmenu.util.mod.Mod;
import com.terraformersmc.modmenu.util.mod.ModSearch;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@IfModLoaded("modmenu")
@Mixin(ModSearch.class)
public class ModMenuModSearchMixin {

    @Inject(method = "passesFilters", at = @At("HEAD"), cancellable = true, remap = false)
    private static void passesFilters(ModsScreen screen, Mod mod, String query, CallbackInfoReturnable<Integer> cir) {
        if (ModMenuConfig.EASTER_EGGS.getValue()) {
            if (query.length() >= 3 && mod.getId().equals(SubtleEffects.MOD_ID) && Util.isSouthEast(query)) {
                cir.setReturnValue(2);
            }
        }
    }
}
