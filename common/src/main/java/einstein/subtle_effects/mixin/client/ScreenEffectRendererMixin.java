package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.PoseStack;
import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @WrapWithCondition(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;renderFire(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
    private static boolean shouldRenderFire(Minecraft minecraft, PoseStack poseStack) {
        return !ModConfigs.GENERAL.fireResistanceDisablesFireRendering || !minecraft.player.hasEffect(MobEffects.FIRE_RESISTANCE);
    }

    @Inject(method = "renderFire", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private static void adjustFireHeight(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
        poseStack.translate(0, ModConfigs.GENERAL.fireHeight.get(), 0);
    }
}
