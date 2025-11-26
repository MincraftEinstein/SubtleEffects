package einstein.subtle_effects.ticking.tickers.entity;

import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.mixin.client.block.AbstractCauldronBlockAccessor;
import einstein.subtle_effects.mixin.common.entity.EntityAccessor;
import einstein.subtle_effects.util.FluidDefinitionAccessor;
import einstein.subtle_effects.util.FluidHeightAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EntityCauldronTicker extends EntityTicker<Entity> {

    private boolean wasInWater;
    private FluidDefinition lastTouchedFluid;

    public EntityCauldronTicker(Entity entity) {
        super(entity);
    }

    @Override
    public void entityTick() {
        BlockPos pos = entity.blockPosition();
        BlockState state = entity.getInBlockState();
        double height = Util.getCauldronFillHeight(state);
        boolean isWater = state.is(Blocks.WATER_CAULDRON);
        Block block = state.getBlock();

        if (height > 0 && ((AbstractCauldronBlockAccessor) block).isEntityInside(state, pos, entity)) {
            if (entity.isOnFire() && (state.is(Blocks.POWDER_SNOW_CAULDRON) || isWater)) {
                ((EntityAccessor) entity).playExtinguishedSound();
            }

            FluidDefinition fluidDefinition = ((FluidDefinitionAccessor) block).subtleEffects$getFluidDefinition();
            if (fluidDefinition != null) {
                if (!fluidDefinition.is(lastTouchedFluid)) {
                    fluidDefinition.splashType().ifPresent(splashType -> {
                        if (ParticleSpawnUtil.spawnSplashEffects(entity, level, fluidDefinition.id(), pos.getY() + height, entity.getDeltaMovement().y())) {
                            if (isWater) {
                                ((FluidHeightAccessor) entity).subtleEffects$cancelNextWaterSplash();
                            }
                        }
                    });
                }
                lastTouchedFluid = fluidDefinition;

                if (!isWater) {
                    return;
                }
            }

            if (isWater && !wasInWater) {
                ((EntityAccessor) entity).doWaterSplashingEffects();
            }
        }

        wasInWater = isWater;
        if (isWater) {
            return;
        }

        lastTouchedFluid = null;
    }
}
