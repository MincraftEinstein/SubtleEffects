package einstein.subtle_effects.mixin.common.entity;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.networking.clientbound.ClientBoundFallingBlockLandPayload;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.FluidDefinitionAccessor;
import einstein.subtle_effects.util.FluidLogicAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.function.Consumers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity implements FluidLogicAccessor {

    @Shadow
    public abstract BlockState getBlockState();

    public FallingBlockEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;handlePortal()V"))
    private void tick(CallbackInfo ci) {
        if (level().isClientSide()) {
            subtleEffects$getFluidDefinitionHeight().clear();
            subtleEffects$updateFluidDefinitionHeight();
            subtleEffects$setLastTouchedFluid(ParticleSpawnUtil.preformSplash(true, true, this, false, Consumers.nop()));
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Fallable;onLand(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/item/FallingBlockEntity;)V"))
    private void onLand(CallbackInfo ci, @Local BlockPos pos) {
        Services.NETWORK.sendToClientsTracking((ServerLevel) level(), pos, new ClientBoundFallingBlockLandPayload(getBlockState(), pos, isInWater()));
    }

    @Unique
    private void subtleEffects$updateFluidDefinitionHeight() {
        if (touchingUnloadedChunk()) {
            return;
        }

        AABB aabb = getBoundingBox();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        Object2DoubleMap<FluidDefinition> fluidHeights = new Object2DoubleArrayMap<>();

        for (int x = Mth.floor(aabb.minX); x < Mth.ceil(aabb.maxX); x++) {
            for (int y = Mth.floor(aabb.minY); y < Mth.ceil(aabb.maxY); y++) {
                for (int z = Mth.floor(aabb.minZ); z < Mth.ceil(aabb.maxZ); z++) {
                    pos.set(x, y, z);
                    FluidState fluidState = level().getFluidState(pos);

                    if (!fluidState.isEmpty()) {
                        double fluidHeight = y + fluidState.getHeight(level(), pos);
                        if (fluidHeight >= aabb.minY) {

                            FluidDefinition fluidDefinition = ((FluidDefinitionAccessor) fluidState.getType()).subtleEffects$getFluidDefinition();
                            fluidHeights.put(fluidDefinition, Math.max(fluidHeight - aabb.minY, fluidHeights.getOrDefault(fluidDefinition, 0)));
                        }
                    }
                }
            }
        }

        subtleEffects$getFluidDefinitionHeight().putAll(fluidHeights);
    }
}
