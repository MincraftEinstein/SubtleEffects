package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.dustClouds")
public class DustCloudsConfig extends ConfigSection {

    public ValidatedFloat scale = new ValidatedFloat(2, 2, 0.5F);
    public ValidatedFloat alpha = new ValidatedFloat(1, 1, 0.3F, ValidatedNumber.WidgetType.TEXTBOX);
    public boolean playerFell = true;
    public boolean playerRunning = true;
    public boolean playerRunningRequiresSpeed = false;
    public boolean mobFell = true;
    public boolean mobRunning = true;
    public boolean landMaceAttack = true;
    public boolean preventWhenRaining = false;
}
