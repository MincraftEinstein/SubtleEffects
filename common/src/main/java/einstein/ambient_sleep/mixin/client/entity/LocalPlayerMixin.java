package einstein.ambient_sleep.mixin.client.entity;

import com.mojang.authlib.GameProfile;
import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.init.ModSounds;
import einstein.ambient_sleep.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    @Unique
    private int ambientSleep$growlTimer = 0;

    public LocalPlayerMixin(ClientLevel level, GameProfile profile) {
        super(level, profile);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
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
}
