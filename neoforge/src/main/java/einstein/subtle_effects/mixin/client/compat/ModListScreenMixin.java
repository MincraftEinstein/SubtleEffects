package einstein.subtle_effects.mixin.client.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.Util;
import net.neoforged.neoforge.client.gui.modlist.ModListScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Locale;
import java.util.Objects;


@Mixin(value = ModListScreen.class, remap = false)
public class ModListScreenMixin {

    @WrapOperation(method = "lambda$updateModsList$0", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"))
    private static boolean filterSouthEast(String modDisplayName, CharSequence searchQuery, Operation<Boolean> original) {
        return original.call(modDisplayName, searchQuery) || (Objects.equals(modDisplayName, SubtleEffects.MOD_NAME.toLowerCase(Locale.ROOT)) && Util.isSouthEast(searchQuery));
    }

}
