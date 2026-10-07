package einstein.subtle_effects.mixin.client.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.init.ModConfigs.ITEMS;

@Mixin(BlockTransformer.class)
public class BlockTransformerMixin {

    @WrapOperation(method = "transformBlock*", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private void evaluateNewBlockState(Level level, Entity entity, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch, Operation<Void> original, @Local(name = "oldBlockState") BlockState oldBlockState) {
        if (level.isClientSide()) {
            if (sound.equals(SoundEvents.AXE_STRIP.value())) {
                if (ITEMS.axeStripParticles.get()) {
                    level.addDestroyBlockEffect(pos, oldBlockState);
                }
            }
        }
        original.call(level, entity, pos, sound, source, volume, pitch);
    }

}