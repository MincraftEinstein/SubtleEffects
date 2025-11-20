package einstein.subtle_effects.ticking.tickers.entity;

import einstein.subtle_effects.data.FluidPair;
import einstein.subtle_effects.data.splash_types.SplashTypeReloadListener;
import einstein.subtle_effects.mixin.client.block.AbstractCauldronBlockAccessor;
import einstein.subtle_effects.mixin.common.entity.EntityAccessor;
import einstein.subtle_effects.util.FluidAccessor;
import einstein.subtle_effects.util.FluidHeightAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EntityCauldronTicker extends EntityTicker<Entity> {

    private boolean wasInWater;
    private FluidPair lastTouchedFluid;

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

            FluidPair fluidPair = ((FluidAccessor) block).subtleEffects$getFluidPair();
            if (fluidPair != null) {
                if (lastTouchedFluid != fluidPair) {
                    ResourceLocation type = SplashTypeReloadListener.FLUID_PAIR_TO_ID.get(fluidPair);

                    if (type != null) {
                        if (ParticleSpawnUtil.spawnSplashEffects(entity, level, type, pos.getY() + height, entity.getDeltaMovement().y())) {
                            if (isWater) {
                                ((FluidHeightAccessor) entity).subtleEffects$cancelNextWaterSplash();
                            }
                        }
                    }
                }
                lastTouchedFluid = fluidPair;

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
