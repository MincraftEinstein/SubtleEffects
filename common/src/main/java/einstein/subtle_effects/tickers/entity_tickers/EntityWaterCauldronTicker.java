package einstein.subtle_effects.tickers.entity_tickers;

import einstein.subtle_effects.mixin.client.entity.EntityAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EntityWaterCauldronTicker extends EntityTicker<Entity> {

    private boolean wasInWaterCauldron;

    public EntityWaterCauldronTicker(Entity entity) {
        super(entity);
    }

    @Override
    public void entityTick() {
        BlockPos pos = entity.blockPosition();
        BlockState state = level.getBlockState(pos);
        double height = Util.getCauldronFillHeight(state);

        if (state.is(Blocks.WATER_CAULDRON) && height > 0) {
            if (pos.getY() + height >= entity.getY()) {
                if (!wasInWaterCauldron) {
                    wasInWaterCauldron = true;
                    ((EntityAccessor) entity).doWaterSplashingEffects();
                }
                return;
            }
        }
        wasInWaterCauldron = false;
    }
}
