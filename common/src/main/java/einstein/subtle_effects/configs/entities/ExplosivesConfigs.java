package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.configs.SmokeType;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.explosives")
public class ExplosivesConfigs extends ConfigSection {

    public boolean tntUpdateSmoke = true;
    public boolean tntFlames = true;
    public boolean tntSparks = true;
    public SmokeType creeperSmoke = SmokeType.UPDATED;
    public boolean creeperSparks = true;
}
