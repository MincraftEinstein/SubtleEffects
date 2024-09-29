package einstein.subtle_effects.configs.entities;

import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

public class SleepingConfigs extends ConfigSection {

    public ValidatedDouble playerSnoreChance = new ValidatedDouble(1, 1, 0, ValidatedNumber.WidgetType.TEXTBOX);
    public ValidatedDouble villagerSnoreChance = new ValidatedDouble(1, 1, 0, ValidatedNumber.WidgetType.TEXTBOX);
    public boolean displaySleepingZsOnlyWhenSnoring = false;
    public boolean foxesHaveSleepingZs = true;
    public boolean adjustNameTagWhenSleeping = true;
    public boolean sleepingZs = true;
}
