package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.sleeping")
public class SleepingConfigs extends ConfigSection {

    public ValidatedDouble playerSnoreChance = new ValidatedDouble(1, 1, 0);
    public ValidatedDouble villagerSnoreChance = new ValidatedDouble(1, 1, 0);
    public boolean displaySleepingZsOnlyWhenSnoring = false;
    public boolean foxesHaveSleepingZs = true;
    public boolean batsHaveSleepingZs = true;
    public boolean catsHaveSleepingZs = true;
    public boolean adjustNameTagWhenSleeping = true;
    public boolean sleepingZs = true;
}
