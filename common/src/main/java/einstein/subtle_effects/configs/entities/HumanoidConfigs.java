package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.configs.ModEntityConfigs;
import einstein.subtle_effects.configs.entities.humanoids.FrostyBreathConfigs;
import einstein.subtle_effects.configs.entities.humanoids.PlayerConfigs;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.humanoids")
public class HumanoidConfigs extends ConfigSection {

    public FrostyBreathConfigs frostyBreath = new FrostyBreathConfigs();
    public PlayerConfigs player = new PlayerConfigs();
    public ModEntityConfigs.PerspectiveDisplayType drowningBubbles = ModEntityConfigs.PerspectiveDisplayType.DEFAULT;
    public ValidatedInt drowningBubblesDensity = new ValidatedInt(10, 15, 3);
}
