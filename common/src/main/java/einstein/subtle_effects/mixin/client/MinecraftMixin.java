package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Final
    public GameRenderer gameRenderer;

    @Shadow
    @Final
    public Options options;

    @Inject(method = "setScreen", at = @At("TAIL"))
    private void setScreen(Screen screen, CallbackInfo ci) {
        if (screen != null && player != null) {
            if (ModConfigs.GENERAL.mobSkullShaders) {
                gameRenderer.clearPostEffect();
                Util.applyHelmetShader(player.getItemBySlot(EquipmentSlot.HEAD), options.getCameraType());
            }
        }
    }
}
