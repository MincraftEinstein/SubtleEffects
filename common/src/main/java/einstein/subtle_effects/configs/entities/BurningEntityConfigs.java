package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.configs.SmokeType;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

public class BurningEntityConfigs extends ConfigSection {

    public SmokeType smoke = SmokeType.DEFAULT;
    public boolean flames = true;
    public boolean sparks = true;
    public boolean sounds = true;
}
