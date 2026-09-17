package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.client.renderer.DebugScreenOverlayRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class FabricGuiMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        DebugScreenOverlayRenderer.render(guiGraphics);
    }
}
