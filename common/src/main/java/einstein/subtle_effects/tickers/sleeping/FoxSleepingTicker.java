package einstein.subtle_effects.tickers.sleeping;

import net.minecraft.world.entity.animal.Fox;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class FoxSleepingTicker extends SleepingTicker<Fox> {

    public FoxSleepingTicker(Fox entity) {
        super(entity);
    }

    @Override
    protected boolean isSleeping() {
        return ENTITIES.sleeping.foxesHaveSleepingZs && super.isSleeping();
    }

    @Override
    protected boolean particleConfigEnabled() {
        return ENTITIES.sleeping.foxesHaveSleepingZs;
    }
}
