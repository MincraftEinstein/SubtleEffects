package einstein.subtle_effects.mixin.common.item;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.networking.clientbound.ClientBoundAnimalFedPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FoodOnAStickItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodOnAStickItem.class)
public class FoodOnAStickItemMixin {

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndConvertOnBreak(ILnet/minecraft/world/level/ItemLike;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"))
    private void addEatEffects(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local Entity entity, @Local ItemStack heldItem) {
        if (level instanceof ServerLevel serverLevel) {
            if (!heldItem.isEmpty()) {
                ItemStack stack = heldItem.is(Items.CARROT_ON_A_STICK) ? new ItemStack(Items.CARROT) : (heldItem.is(Items.WARPED_FUNGUS_ON_A_STICK) ? new ItemStack(Items.WARPED_FUNGUS) : heldItem);
                Services.NETWORK.sendToClientsTracking(serverLevel, entity.blockPosition(), new ClientBoundAnimalFedPayload(entity.getId(), stack));
            }
        }
    }
}
