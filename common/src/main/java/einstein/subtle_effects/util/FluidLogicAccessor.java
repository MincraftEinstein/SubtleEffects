package einstein.subtle_effects.util;

import einstein.subtle_effects.data.FluidDefinition;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.function.Consumers;
import org.jetbrains.annotations.Nullable;

public interface FluidLogicAccessor {

    Object2DoubleMap<FluidDefinition> subtleEffects$getFluidDefinitionHeight();

    @Nullable
    FluidDefinition subtleEffects$getLastTouchedFluid();

    void subtleEffects$setLastTouchedFluid(@Nullable FluidDefinition fluidDefinition);

    void subtleEffects$cancelNextWaterSplash();

    static void clientUpdateInWaterStateAndDoFluidPushing(Entity entity) {
        if (entity.level().isClientSide) {
            FluidLogicAccessor accessor = (FluidLogicAccessor) entity;
            accessor.subtleEffects$getFluidDefinitionHeight().clear();
            subtleEffects$updateFluidDefinitionHeight(entity, entity.getBoundingBox());
            // Minecarts and falling blocks don't need to cancel the vanilla splash effects,
            // since Minecarts only call 'doWaterSplashEffect' on the server and falling blocks don't call it at all
            accessor.subtleEffects$setLastTouchedFluid(ParticleSpawnUtil.preformSplash(true, true, entity, false, Consumers.nop(), entity.getDeltaMovement().y(), entity.getY(), entity.blockPosition()));
        }
    }

    static void subtleEffects$updateFluidDefinitionHeight(Entity entity, AABB boundingBox) {
        if (entity.touchingUnloadedChunk()) {
            return;
        }

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        Object2DoubleMap<FluidDefinition> fluidHeights = new Object2DoubleArrayMap<>();
        Level level = entity.level();

        for (int x = Mth.floor(boundingBox.minX); x < Mth.ceil(boundingBox.maxX); x++) {
            for (int y = Mth.floor(boundingBox.minY); y < Mth.ceil(boundingBox.maxY); y++) {
                for (int z = Mth.floor(boundingBox.minZ); z < Mth.ceil(boundingBox.maxZ); z++) {
                    pos.set(x, y, z);
                    FluidState fluidState = level.getFluidState(pos);

                    if (!fluidState.isEmpty()) {
                        double fluidHeight = y + fluidState.getHeight(level, pos);
                        if (fluidHeight >= boundingBox.minY) {

                            FluidDefinition fluidDefinition = ((FluidDefinitionAccessor) fluidState.getType()).subtleEffects$getFluidDefinition();
                            fluidHeights.put(fluidDefinition, Math.max(fluidHeight - boundingBox.minY, fluidHeights.getOrDefault(fluidDefinition, 0)));
                        }
                    }
                }
            }
        }

        ((FluidLogicAccessor) entity).subtleEffects$getFluidDefinitionHeight().putAll(fluidHeights);
    }
}
