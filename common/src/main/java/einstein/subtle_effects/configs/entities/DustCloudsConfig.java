package einstein.subtle_effects.configs.entities;

import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

public class DustCloudsConfig extends ConfigSection {

    public ValidatedFloat scale = new ValidatedFloat(1, 2, 0.5F, ValidatedNumber.WidgetType.TEXTBOX);
    public ValidatedFloat alpha = new ValidatedFloat(1, 1, 0.3F, ValidatedNumber.WidgetType.TEXTBOX);
    public boolean fallDamage = true;
    public boolean sprinting = true;
    public boolean mobSprinting = true;
}
