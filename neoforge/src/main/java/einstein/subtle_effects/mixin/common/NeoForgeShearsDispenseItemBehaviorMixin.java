package einstein.subtle_effects.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.networking.clientbound.ClientBoundSheepShearPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsDispenseItemBehavior.class)
public class NeoForgeShearsDispenseItemBehaviorMixin {

    // Thank you neoforge ever so much for add an extra parameter
    @Inject(method = "tryShearLivingEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V"))
    private static void spawnShearParticles(ServerLevel level, BlockPos pos, ItemStack stack, CallbackInfoReturnable<Boolean> cir, @Local LivingEntity entity) {
        if (entity instanceof Sheep sheep) {
            Services.NETWORK.sendToClientsTracking(level, pos, new ClientBoundSheepShearPayload(sheep.getId()));
        }
    }
}
