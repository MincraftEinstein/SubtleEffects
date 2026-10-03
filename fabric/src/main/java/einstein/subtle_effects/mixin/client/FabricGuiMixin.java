package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.client.renderer.DebugScreenOverlayRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class FabricGuiMixin {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void init(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        DebugScreenOverlayRenderer.extract(graphics, deltaTracker);
    }
}
