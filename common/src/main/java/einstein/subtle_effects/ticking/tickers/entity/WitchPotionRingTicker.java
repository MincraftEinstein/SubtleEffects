package einstein.subtle_effects.ticking.tickers.entity;

import net.minecraft.world.entity.monster.Witch;

public class WitchPotionRingTicker extends HumanoidPotionRingTicker<Witch> {

    public WitchPotionRingTicker(Witch entity) {
        super(entity);
    }

    @Override
    protected boolean isUsingItem() {
        return entity.isDrinkingPotion();
    }
}
