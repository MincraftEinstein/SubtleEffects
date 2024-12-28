package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.util.Util;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownEgg;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEgg.class)
public class ThrownEggMixin {

    @Inject(method = "handleEntityEvent", at = @At("TAIL"))
    private void handle(byte id, CallbackInfo ci) {
        if (id == 3) {
            Util.playClientSound(((Entity) (Object) this).blockPosition(), ModSounds.EGG_BREAK.get(), SoundSource.PLAYERS, 1F, 1F);
        }
    }
}
