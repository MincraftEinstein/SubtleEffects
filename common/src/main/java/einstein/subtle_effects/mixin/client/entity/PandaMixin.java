package einstein.subtle_effects.mixin.client.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Panda.class)
public abstract class PandaMixin {

    // Inject into the interactAt method to detect right-click on entity
    @Inject(method = "interactAt", at = @At("HEAD"), cancellable = true)
    private void onInteractAt(Player player, Vec3 hitVec, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (level().isClientSide() && ModConfigs.ENTITIES.pandaFeatherSneeze) {
        Panda panda = (Panda)(Object)this;
        Level level = panda.level();

        // Only run on server side
        if (!level.isClientSide) {
            // Check if panda is baby
            if (panda.isBaby()) {
                // Check if player is holding a feather
                if (player.getItemInHand(hand).is(Items.FEATHER)) {
                    panda.sneeze(false)
                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        }
        }
    }
}
