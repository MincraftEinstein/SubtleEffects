package einstein.subtle_effects.mixin.client.item;

import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Inject(method = "useOn", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;AXE_STRIP:Lnet/minecraft/sounds/SoundEvent;"))
    private void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (ModConfigs.ITEMS.axeStripParticles) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();

            if (level.isClientSide) {
                level.addDestroyBlockEffect(pos, level.getBlockState(pos));
            }
        }
    }
}
