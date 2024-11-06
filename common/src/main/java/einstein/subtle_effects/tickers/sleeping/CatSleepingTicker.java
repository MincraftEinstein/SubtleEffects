package einstein.subtle_effects.tickers.sleeping;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class CatSleepingTicker extends SleepingTicker<Cat> {

    public CatSleepingTicker(Cat cat) {
        super(cat);
    }

    @Override
    protected boolean isSleeping() {
        return ENTITIES.sleeping.catsHaveSleepingZs && entity.isLying();
    }

    @Override
    protected boolean shouldDelay() {
        LivingEntity owner = entity.getOwner();
        return owner != null && owner.equals(Minecraft.getInstance().player) && owner.isSleeping();
    }

    @Override
    protected boolean particleConfigEnabled() {
        return ENTITIES.sleeping.catsHaveSleepingZs;
    }
}
