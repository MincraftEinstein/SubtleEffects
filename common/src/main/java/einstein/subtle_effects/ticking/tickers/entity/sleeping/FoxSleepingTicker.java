package einstein.subtle_effects.ticking.tickers.entity.sleeping;

import net.minecraft.world.entity.animal.fox.Fox;

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
