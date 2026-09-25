package einstein.subtle_effects.mixin.common.entity;

import einstein.subtle_effects.mixin.common.block.AbstractCauldronBlockAccessor;
import einstein.subtle_effects.networking.PayloadSender;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntityLandInFluidPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class CommonEntityMixin {

    @Shadow
    protected boolean firstTick;

    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @Nullable
    @Unique
    private Object subtleEffects$serverLastTouchedFluid;

    @Inject(method = "updateInWaterStateAndDoFluidPushing", at = @At("TAIL"))
    private void sendServerPlayerSplashes(CallbackInfoReturnable<Boolean> cir) {
        if (subtleEffects$me instanceof ServerPlayer serverPlayer && !firstTick) {
            Level level = subtleEffects$me.level();
            BlockPos pos = subtleEffects$me.blockPosition();
            BlockState state = level.getBlockState(pos);
            FluidState fluidState = level.getFluidState(pos);
            Fluid fluid = fluidState.getType();
            Block block = state.getBlock();
            boolean isEmptyFluid = fluidState.isEmpty();
            Object touchedFluid = isEmptyFluid ? block : fluid;

            if ((subtleEffects$serverLastTouchedFluid instanceof Fluid lastfluid && !fluid.isSame(lastfluid)) || subtleEffects$serverLastTouchedFluid != touchedFluid) {
                subtleEffects$serverLastTouchedFluid = touchedFluid;
                boolean isCauldron = block instanceof AbstractCauldronBlockAccessor;
                if (!isEmptyFluid || (isCauldron && ((AbstractCauldronBlockAccessor) block).isEntityInside(state, pos, serverPlayer))) {
                    PayloadSender.sendToClientsTracking(serverPlayer, (ServerLevel) level, pos,
                            new ClientBoundEntityLandInFluidPayload(serverPlayer.getId(), serverPlayer.getY(),
                                    serverPlayer.getDeltaMovement().y(), pos, isCauldron)
                    );
                }
            }
        }
    }
}
