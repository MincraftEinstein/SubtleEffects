package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private static void setupAlpha(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci, @Share("alpha") LocalFloatRef alphaRef) {
        float fireResAlpha = ModConfigs.GENERAL.fireOverlayAlphaWithFireResistance.get();
        float alpha = ModConfigs.GENERAL.fireOverlayAlpha.get();

        if (alpha == 0 && fireResAlpha == 0) {
            ci.cancel();
            return;
        }

        // noinspection ConstantConditions
        alphaRef.set(minecraft.player.hasEffect(MobEffects.FIRE_RESISTANCE) && fireResAlpha != 0 ? fireResAlpha : alpha);
    }

    @Inject(method = "renderFire", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private static void adjustFireHeight(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
        poseStack.translate(0, ModConfigs.GENERAL.fireOverlayHeight.get(), 0);
    }

    @WrapOperation(method = "renderFire", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private static VertexConsumer modifyAlpha(VertexConsumer consumer, float red, float green, float blue, float alpha, Operation<VertexConsumer> original, @Share("alpha") LocalFloatRef alphaRef) {
        return original.call(consumer, red, green, blue, alphaRef.get());
    }
}
