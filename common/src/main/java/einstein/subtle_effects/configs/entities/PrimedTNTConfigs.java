package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.primedTNT")
public class PrimedTNTConfigs extends ConfigSection {

    public boolean updateSmoke = true;
    public boolean flames = true;
    public boolean sparks = true;
}
