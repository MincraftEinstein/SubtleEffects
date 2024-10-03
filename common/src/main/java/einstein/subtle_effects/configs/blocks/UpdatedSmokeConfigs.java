package einstein.subtle_effects.configs.blocks;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

@Translation(prefix = ModConfigs.BASE_KEY + "blocks.updatedSmoke")
public class UpdatedSmokeConfigs extends ConfigSection {

    public boolean candleSmoke = true;
    public boolean furnaceSmoke = true;
    public boolean fireSmoke = true;
    public boolean torchSmoke = true;
    public boolean lavaSparkSmoke = true;
}
