package einstein.subtle_effects.mixin.common;

import einstein.subtle_effects.networking.clientbound.ClientBoundStonecutterUsedPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.inventory.StonecutterMenu$2")
public class FabricStonecutterMenu$2Mixin {

    @Shadow
    @Final
    StonecutterMenu field_17639; // if intellij says the shadowed field doesn't exist, it's crazy

    @Unique
    private ItemStack subtleEffects$inputStack;

    @Inject(method = "onTake", at = @At("HEAD"))
    private void onTake(Player player, ItemStack stack, CallbackInfo ci) {
        if (!player.level().isClientSide()) {
            subtleEffects$inputStack = ((StonecutterMenuAccessor) field_17639).getInputSlot().getItem().copy();
        }
    }

    // if intellij says the method being injected into doesn't exist, it's crazy
    @Inject(method = "method_17868", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private void spawnParticles(Level level, BlockPos pos, CallbackInfo ci) {
        if (subtleEffects$inputStack != null && !subtleEffects$inputStack.isEmpty()) {
            Services.NETWORK.sendToClientsTracking((ServerLevel) level, pos, new ClientBoundStonecutterUsedPayload(pos, subtleEffects$inputStack));
            subtleEffects$inputStack = null;
        }
    }
}
