package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.attacked")
public class EntityAttackedConfigs extends ConfigSection {

    public boolean chickenFeathers = true;
    public boolean parrotFeathers = true;
    public boolean snowGolemSnowflakes = true;
}
