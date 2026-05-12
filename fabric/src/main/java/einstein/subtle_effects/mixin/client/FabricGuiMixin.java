package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.SubtleEffectsClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.LayeredDraw;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class FabricGuiMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci, @Local(ordinal = 0) LayeredDraw layeredDraw) {
        layeredDraw.add(SubtleEffectsClient::renderDebugScreenOverlay);
    }
}
