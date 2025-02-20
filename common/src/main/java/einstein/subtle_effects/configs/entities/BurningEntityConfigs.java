package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.configs.SmokeType;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.burning")
public class BurningEntityConfigs extends ConfigSection {

    public SmokeType smoke = SmokeType.DEFAULT;
    public boolean flames = true;
    public boolean sparks = true;
    public boolean sounds = true;
    public boolean extinguishSteam = true;
}
