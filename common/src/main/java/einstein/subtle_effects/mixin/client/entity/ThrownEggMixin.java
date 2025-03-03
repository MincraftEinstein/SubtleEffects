package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ThrownEgg;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEgg.class)
public class ThrownEggMixin {

    @Unique
    private final ThrownEgg subtleEffects$me = (ThrownEgg) (Object) this;

    @Inject(method = "handleEntityEvent", at = @At("TAIL"))
    private void handle(byte id, CallbackInfo ci) {
        float volume = ModConfigs.ENTITIES.eggSmashSoundVolume.get();

        if (id == 3 && volume > 0) {
            subtleEffects$me.level().playSound(Minecraft.getInstance().player, subtleEffects$me.getX(), subtleEffects$me.getY(), subtleEffects$me.getZ(), ModSounds.EGG_BREAK.get(), SoundSource.PLAYERS, volume, Mth.nextFloat(subtleEffects$me.getRandom(), 0.7F, 1.5F));
        }
    }
}
