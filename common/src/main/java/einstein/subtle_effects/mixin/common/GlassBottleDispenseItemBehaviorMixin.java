package einstein.subtle_effects.mixin.common;

import einstein.subtle_effects.networking.clientbound.ClientBoundDispenseBucketPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.core.dispenser.DispenseItemBehavior$14")
public class GlassBottleDispenseItemBehaviorMixin {

    @Inject(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/PotionContents;createItemStack(Lnet/minecraft/world/item/Item;Lnet/minecraft/core/Holder;)Lnet/minecraft/world/item/ItemStack;"))
    private void execute(BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        BlockPos pos = source.pos();
        Services.NETWORK.sendToClientsTracking(source.level(), pos, new ClientBoundDispenseBucketPayload(new ItemStack(Items.WATER_BUCKET), pos));
    }
}
