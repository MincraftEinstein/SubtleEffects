package einstein.subtle_effects.mixin.client.block;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeBlock.class)
public class SlimeBlockMixin {

    @Inject(method = "fallOn", at = @At("HEAD"))
    private void bounceUp(Level level, BlockState state, BlockPos pos, Entity entity, float distance, CallbackInfo ci) {
        if (entity.level().isClientSide && ModConfigs.BLOCKS.slimeBlockBounceSounds) {
            if (distance > 1) {
                if (distance < 4) {
                    Util.playClientSound(entity.getSoundSource(), entity, SoundEvents.SLIME_SQUISH_SMALL, 0.5F, 1);
                    return;
                }

                Util.playClientSound(entity.getSoundSource(), entity, SoundEvents.SLIME_SQUISH, 1, 1);
            }
        }
    }
}
