package einstein.subtle_effects.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.networking.clientbound.ClientBoundCompostItemPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.level.block.ComposterBlock$InputContainer")
public class ComposterBlockInputContainerMixin {

    @Shadow
    @Final
    private LevelAccessor level;

    @Shadow
    @Final
    private BlockPos pos;

    @Inject(method = "setChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;levelEvent(ILnet/minecraft/core/BlockPos;I)V"))
    private void setChanged(CallbackInfo ci, @Local ItemStack stack) {
        if (level instanceof ServerLevel serverLevel) {
            Services.NETWORK.sendToClientsTracking(serverLevel, pos, new ClientBoundCompostItemPayload(stack, pos));
        }
    }
}
