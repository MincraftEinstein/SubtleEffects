package einstein.subtle_effects.mixin.common.block.dispenser;

import einstein.subtle_effects.networking.clientbound.ClientBoundDispenseBucketPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.core.dispenser.DispenseItemBehavior$4")
public class FilledBucketDispenseItemBehaviorMixin {

    @Inject(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/dispenser/DispenseItemBehavior$4;consumeWithRemainder(Lnet/minecraft/core/dispenser/BlockSource;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"))
    private void execute(BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stackCopy = stack.copy();
        if (!stackCopy.isEmpty()) {
            BlockPos pos = source.pos();
            Services.NETWORK.sendToClientsTracking(source.level(), pos, new ClientBoundDispenseBucketPayload(stackCopy, pos));
        }
    }
}
