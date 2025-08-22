package einstein.subtle_effects.mixin.common.block;

import einstein.subtle_effects.networking.clientbound.ClientBoundCompostItemPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ComposterBlock.class)
public class CommonComposterMixin {

    @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;levelEvent(ILnet/minecraft/core/BlockPos;I)V"))
    private void useItem(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (level instanceof ServerLevel serverLevel) {
            ItemStack copiedStack = stack.copy();

            if (!copiedStack.isEmpty()) {
                Services.NETWORK.sendToClientsTracking(serverLevel, pos, new ClientBoundCompostItemPayload(copiedStack, pos, false));
            }
        }
    }

    @Mixin(targets = "net.minecraft.world.level.block.ComposterBlock$InputContainer")
    public static abstract class InputContainerMixin implements WorldlyContainer {

        @Shadow
        @Final
        private LevelAccessor level;

        @Shadow
        @Final
        private BlockPos pos;

        @Inject(method = "setChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;levelEvent(ILnet/minecraft/core/BlockPos;I)V"))
        private void setChanged(CallbackInfo ci) {
            if (level instanceof ServerLevel serverLevel) {
                ItemStack stack = getItem(0).copy();

                if (!stack.isEmpty()) {
                    Services.NETWORK.sendToClientsTracking(serverLevel, pos, new ClientBoundCompostItemPayload(stack, pos, false));
                }
            }
        }
    }
}
