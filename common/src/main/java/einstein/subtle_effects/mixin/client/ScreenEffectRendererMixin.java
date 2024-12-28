package einstein.subtle_effects.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Inject(method = "renderFire", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private static void adjustFireHeight(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
        poseStack.translate(0, ModConfigs.GENERAL.fireHeight.get(), 0);
    }
}
