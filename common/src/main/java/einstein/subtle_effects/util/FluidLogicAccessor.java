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
            subtleEffects$updateFluidDefinitionHeight(entity);
            accessor.subtleEffects$setLastTouchedFluid(ParticleSpawnUtil.preformSplash(true, true, entity, false, Consumers.nop()));
        }
    }

    static void subtleEffects$updateFluidDefinitionHeight(Entity entity) {
        if (entity.touchingUnloadedChunk()) {
            return;
        }

        AABB aabb = entity.getBoundingBox();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        Object2DoubleMap<FluidDefinition> fluidHeights = new Object2DoubleArrayMap<>();
        Level level = entity.level();

        for (int x = Mth.floor(aabb.minX); x < Mth.ceil(aabb.maxX); x++) {
            for (int y = Mth.floor(aabb.minY); y < Mth.ceil(aabb.maxY); y++) {
                for (int z = Mth.floor(aabb.minZ); z < Mth.ceil(aabb.maxZ); z++) {
                    pos.set(x, y, z);
                    FluidState fluidState = level.getFluidState(pos);

                    if (!fluidState.isEmpty()) {
                        double fluidHeight = y + fluidState.getHeight(level, pos);
                        if (fluidHeight >= aabb.minY) {

                            FluidDefinition fluidDefinition = ((FluidDefinitionAccessor) fluidState.getType()).subtleEffects$getFluidDefinition();
                            fluidHeights.put(fluidDefinition, Math.max(fluidHeight - aabb.minY, fluidHeights.getOrDefault(fluidDefinition, 0)));
                        }
                    }
                }
            }
        }

        ((FluidLogicAccessor) entity).subtleEffects$getFluidDefinitionHeight().putAll(fluidHeights);
    }
}
