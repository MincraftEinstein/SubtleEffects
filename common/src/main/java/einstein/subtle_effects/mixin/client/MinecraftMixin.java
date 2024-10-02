package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
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

    @Inject(method = "setScreen", at = @At("TAIL"))
    private void setScreen(Screen screen, CallbackInfo ci) {
        if (screen != null && player != null) {
            if (ModConfigs.GENERAL.mobSkullShaders) {
                Util.applyHelmetShader(player.getInventory().getArmor(3));
            }
        }
    }
}
