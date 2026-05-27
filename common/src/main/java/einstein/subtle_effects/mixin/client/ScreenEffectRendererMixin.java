package einstein.subtle_effects.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Inject(method = "submitFire", at = @At("HEAD"), cancellable = true)
    private static void setupAlpha(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, TextureAtlasSprite sprite, CallbackInfo ci) {
        float fireResAlpha = ModConfigs.GENERAL.fireOverlayAlphaWithFireResistance.get();
        float alpha = ModConfigs.GENERAL.fireOverlayAlpha.get();

        if (alpha == 0 && fireResAlpha == 0) {
            ci.cancel();
        }
    }

    @ModifyArg(method = "lambda$submitFire$0", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;translate(FFF)Lorg/joml/Matrix4f;"), index = 1)
    private static float adjustFireHeight(float x) {
        return x + ModConfigs.GENERAL.fireOverlayHeight.get();
    }

    @ModifyArg(method = "buildFireQuad", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;buildSpriteQuad(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lorg/joml/Matrix4f;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;FFFFFI)V"))
    private static int modifyAlpha(int color) {
        float fireResAlpha = ModConfigs.GENERAL.fireOverlayAlphaWithFireResistance.get();
        float baseAlpha = ModConfigs.GENERAL.fireOverlayAlpha.get();
//         noinspection ConstantConditions
        int alpha = (int) ((Minecraft.getInstance().player.hasEffect(MobEffects.FIRE_RESISTANCE) && fireResAlpha != 0 ? fireResAlpha : baseAlpha) * 255);

        return (color & 0x00ffffff) | (alpha << 24);
    }
}