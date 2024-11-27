package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.configs.SmokeType;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.primedTNT")
public class ExplosivesConfigs extends ConfigSection {

    public boolean TNTUpdateSmoke = true;
    public boolean TNTFlames = true;
    public boolean TNTSparks = true;
    public SmokeType creeperSmoke = SmokeType.UPDATED;
    public boolean creeperSparks = true;
}
