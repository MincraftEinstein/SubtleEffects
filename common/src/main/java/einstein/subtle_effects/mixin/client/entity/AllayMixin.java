package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.util.Util;
import net.minecraft.world.entity.animal.allay.Allay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Allay.class)
public class AllayMixin {

    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/allay/Allay;duplicateAllay()V"))
    public void playAllayDuplicateSound(CallbackInfoReturnable<Boolean> cir) {
        Allay allay = (Allay) (Object) this;
        if (allay.level().isClientSide() && ModConfigs.ENTITIES.allayDuplicatedSounds) {
            Util.playClientSound(allay.blockPosition(), ModSounds.ALLAY_DUPLICATE.get(), allay.getSoundSource(), 1, 1);
        }
    }
}
