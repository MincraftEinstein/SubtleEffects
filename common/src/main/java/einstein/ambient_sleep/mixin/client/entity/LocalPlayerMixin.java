package einstein.ambient_sleep.mixin.client.entity;

import com.mojang.authlib.GameProfile;
import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.init.ModSounds;
import einstein.ambient_sleep.util.Util;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    @Unique
    private int ambientSleep$growlTimer = 0;

    @Unique
    private ItemStack ambientSleep$oldHelmetStack = ItemStack.EMPTY;

    @Unique
    private CameraType ambientSleep$oldCameraType;

    public LocalPlayerMixin(ClientLevel level, GameProfile profile) {
        super(level, profile);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (ModConfigs.INSTANCE.mobSkullShaders.get()) {
            ambientSleep$tryApplyHelmetShader();
        }

        if (!ModConfigs.INSTANCE.stomachGrowling.get()) {
            return;
        }

        if (isCreative() || isSpectator()) {
            return;
        }

        int foodLevel = getFoodData().getFoodLevel();
        if (foodLevel <= 6) {
            if (ambientSleep$growlTimer == 0) {
                Util.playClientSound(SoundSource.PLAYERS, this, ModSounds.PLAYER_STOMACH_GROWL.get(), 1, (random.nextBoolean() ? 1 : 1.5F));
            }

            ambientSleep$growlTimer++;

            if (ambientSleep$growlTimer >= Util.STOMACH_GROWL_DELAY) {
                ambientSleep$growlTimer = 0;
            }
        }
    }

    @Unique
    private void ambientSleep$tryApplyHelmetShader() {
        ItemStack helmetStack = getInventory().getArmor(3);
        if ((ambientSleep$oldHelmetStack.isEmpty() != helmetStack.isEmpty())
                || !ItemStack.isSameItem(ambientSleep$oldHelmetStack, helmetStack)) {
            ambientSleep$oldHelmetStack = helmetStack.copy();
            Util.applyHelmetShader(helmetStack);
        }

        CameraType cameraType = Minecraft.getInstance().options.getCameraType();
        if (ambientSleep$oldCameraType != cameraType) {
            ambientSleep$oldCameraType = cameraType;

            if (cameraType.isFirstPerson()) {
                Util.applyHelmetShader(helmetStack);
            }
        }
    }
}
