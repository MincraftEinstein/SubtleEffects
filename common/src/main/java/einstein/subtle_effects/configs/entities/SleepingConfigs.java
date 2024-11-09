package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.sleeping")
public class SleepingConfigs extends ConfigSection {

    public ValidatedDouble playerSnoreChance = new ValidatedDouble(1, 1, 0);
    public ValidatedFloat playerSnoreSoundVolume = new ValidatedFloat(1, 1, 0);
    public boolean playersHaveSleepingZs = true;
    public ValidatedDouble villagerSnoreChance = new ValidatedDouble(1, 1, 0);
    public ValidatedFloat villagerSnoreSoundVolume = new ValidatedFloat(1, 1, 0);
    public boolean villagersHaveSleepingZs = true;
    public boolean foxesHaveSleepingZs = true;
    public boolean batsHaveSleepingZs = true;
    public boolean catsHaveSleepingZs = true;
    public boolean otherMobsHaveSleepingZs = true;
    public boolean displaySleepingZsOnlyWhenSnoring = false;
    public boolean adjustNameTagWhenSleeping = true;
}
