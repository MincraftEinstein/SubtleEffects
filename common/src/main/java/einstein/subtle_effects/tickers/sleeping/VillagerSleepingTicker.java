package einstein.subtle_effects.tickers.sleeping;

import einstein.subtle_effects.init.ModSounds;
import net.minecraft.world.entity.npc.Villager;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class VillagerSleepingTicker extends SleepingTicker<Villager> {

    public VillagerSleepingTicker(Villager villager) {
        super(villager,
                doesEntitySnore(villager, ENTITIES.sleeping.villagerSnoreChance.get()),
                80,
                ModSounds.VILLAGER_SNORE.get(),
                ENTITIES.sleeping.villagerSnoreSoundVolume.get()
        );
    }

    @Override
    protected boolean particleConfigEnabled() {
        return ENTITIES.sleeping.villagersHaveSleepingZs;
    }
}
