package einstein.subtle_effects.mixin.common.block.dispenser;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.networking.clientbound.ClientBoundSheepShearPayload;
import einstein.subtle_effects.networking.PayloadSender;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsDispenseItemBehavior.class)
public class ForgeShearsDispenseItemBehaviorMixin {

<<<<<<<< HEAD:forge/src/main/java/einstein/subtle_effects/mixin/common/ForgeShearsDispenseItemBehaviorMixin.java
    @Inject(method = "tryShearLivingEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/core/BlockPos;)V"))
    private static void spawnShearParticles(ServerLevel level, BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local LivingEntity entity) {
========
    // Thank you neoforge ever so much for adding an extra parameter
    @Inject(method = "tryShearLivingEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V"))
    private static void spawnShearParticles(ServerLevel level, BlockPos pos, ItemStack stack, CallbackInfoReturnable<Boolean> cir, @Local LivingEntity entity) {
>>>>>>>> main:forge/src/main/java/einstein/subtle_effects/mixin/common/block/dispenser/NeoForgeShearsDispenseItemBehaviorMixin.java
        if (entity instanceof Sheep sheep) {
            PayloadSender.sendToClientsTracking(level, pos, new ClientBoundSheepShearPayload(sheep.getId()));
        }
    }
}
