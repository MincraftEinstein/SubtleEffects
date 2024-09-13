package einstein.subtle_effects.mixin.client.item;

import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Inject(method = "evaluateNewBlockState", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;AXE_STRIP:Lnet/minecraft/sounds/SoundEvent;"))
    private void evaluateNewBlockState(Level level, BlockPos pos, Player player, BlockState state, CallbackInfoReturnable<Optional<BlockState>> cir) {
        if (ModConfigs.INSTANCE.axeStripParticles.get()) {
            if (level.isClientSide) {
                level.addDestroyBlockEffect(pos, state);
            }
        }
    }
}
