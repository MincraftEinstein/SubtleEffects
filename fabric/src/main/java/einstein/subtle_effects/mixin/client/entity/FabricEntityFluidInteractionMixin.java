package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.util.FluidDefinitionAccessor;
import einstein.subtle_effects.util.FluidLogicAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityFluidInteraction.class)
public class FabricEntityFluidInteractionMixin {

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D"),
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/EntityFluidInteraction$Tracker;eyesInside:Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;getFlow(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/Vec3;")
            )
    )
    private void updateFluidPairHeight(Entity entity, boolean ignoreCurrent, CallbackInfoReturnable<Boolean> cir, @Local(name = "fluidState") FluidState fluidState, @Local(name = "tracker") EntityFluidInteraction.Tracker tracker) {
        if (entity.level().isClientSide()) {
            FluidDefinition fluidDefinition = ((FluidDefinitionAccessor) fluidState.getType()).subtleEffects$getFluidDefinition();
            if (fluidDefinition != null) {
                ((FluidLogicAccessor) entity).subtleEffects$getFluidDefinitionHeight().put(fluidDefinition, tracker.height);
            }
        }
    }
}
