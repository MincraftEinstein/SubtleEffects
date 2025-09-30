package einstein.subtle_effects.mixin.common.block.dispenser;

import einstein.subtle_effects.networking.clientbound.ClientBoundDispenseBucketPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.core.dispenser.DispenseItemBehavior$16")
public class FilledBucketDispenseItemBehaviorMixin {

    @Inject(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/DispensibleContainerItem;checkExtraContent(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/BlockPos;)V"))
    private void execute(BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stackCopy = stack.copy();
        if (!stackCopy.isEmpty()) {
            BlockPos pos = source.getPos();
            Services.NETWORK.sendToClientsTracking(source.getLevel(), pos, new ClientBoundDispenseBucketPayload(stackCopy, pos));
        }
    }
}
