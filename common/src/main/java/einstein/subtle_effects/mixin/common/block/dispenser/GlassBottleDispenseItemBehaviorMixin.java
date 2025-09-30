package einstein.subtle_effects.mixin.common.block.dispenser;

import einstein.subtle_effects.networking.clientbound.ClientBoundDispenseBucketPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.core.dispenser.DispenseItemBehavior$24")
public class GlassBottleDispenseItemBehaviorMixin {

    @Inject(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/PotionUtils;setPotion(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/world/item/ItemStack;"))
    private void execute(BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        BlockPos pos = source.getPos();
        Services.NETWORK.sendToClientsTracking(source.getLevel(), pos, new ClientBoundDispenseBucketPayload(new ItemStack(Items.WATER_BUCKET), pos));
    }
}
