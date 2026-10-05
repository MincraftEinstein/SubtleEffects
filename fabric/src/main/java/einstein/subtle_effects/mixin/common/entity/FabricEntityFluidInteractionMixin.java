package einstein.subtle_effects.mixin.common.entity;

import einstein.subtle_effects.mixin.common.block.AbstractCauldronBlockAccessor;
import einstein.subtle_effects.networking.PayloadSender;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntityLandInFluidPayload;
import einstein.subtle_effects.util.FirstTickAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.FluidDefinitionAccessor;
import einstein.subtle_effects.util.FluidLogicAccessor;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityFluidInteraction.class)
public class FabricEntityFluidInteractionMixin {

    @Nullable
    @Unique
    private Object subtleEffects$serverLastTouchedFluid;

    @Inject(method = "update", at = @At("TAIL"))
    private void sendServerPlayerSplashes(Entity entity, boolean ignoreCurrent, CallbackInfo ci) {
        if (entity instanceof ServerPlayer serverPlayer && !((FirstTickAccessor) entity).subtleEffects$isFirstTick()) {
            Level level = entity.level();
            BlockPos pos = entity.blockPosition();
            BlockState state = level.getBlockState(pos);
            FluidState fluidState = level.getFluidState(pos);
            Fluid fluid = fluidState.getType();
            Block block = state.getBlock();
            boolean isEmptyFluid = fluidState.isEmpty();
            Object touchedFluid = isEmptyFluid ? block : fluid;

            if ((subtleEffects$serverLastTouchedFluid instanceof Fluid lastfluid && !fluid.isSame(lastfluid)) || subtleEffects$serverLastTouchedFluid != touchedFluid) {
                subtleEffects$serverLastTouchedFluid = touchedFluid;
                boolean isCauldron = block instanceof AbstractCauldronBlockAccessor;
                if (!isEmptyFluid || (isCauldron && Util.isEntityInsideContent(state, pos, serverPlayer))) {
                    PayloadSender.sendToClientsTracking(serverPlayer, (ServerLevel) level, pos,
                            new ClientBoundEntityLandInFluidPayload(serverPlayer.getId(), serverPlayer.getY(),
                                    serverPlayer.getDeltaMovement().y(), pos, isCauldron)
                    );
                }
            });
        }
    }

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityFluidInteraction$Tracker;accumulateCurrent(Lnet/minecraft/world/phys/Vec3;)V"))
    private void updateFluidPairHeight(Entity entity, boolean ignoreCurrent, CallbackInfo ci, @Local(name = "fluidState") FluidState fluidState, @Local(name = "tracker") EntityFluidInteraction.Tracker tracker) {
        if (entity.level().isClientSide()) {
            FluidDefinition fluidDefinition = ((FluidDefinitionAccessor) fluidState.getType()).subtleEffects$getFluidDefinition();
            if (fluidDefinition != null) {
                ((FluidLogicAccessor) entity).subtleEffects$getFluidDefinitionHeight().put(fluidDefinition, tracker.height);
            }
        }
    }
}
