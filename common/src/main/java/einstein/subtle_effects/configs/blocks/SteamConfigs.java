package einstein.subtle_effects.configs.blocks;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

@Translation(prefix = ModConfigs.BASE_KEY + "blocks.steam")
public class SteamConfigs extends ConfigSection {

    public boolean lavaFizzSteam = true;
    public boolean replaceCampfireFoodSmoke = true;
    public boolean spongeDryingOutSteam = true;
    public boolean steamingWater = false;
    public boolean boilingWater = false;
    public boolean steamingWaterCauldron = false;
    public boolean boilingWaterCauldron = false;
    public ValidatedInt steamingThreshold = new ValidatedInt(11, 14, 1);
    public ValidatedInt boilingThreshold = new ValidatedInt(13, 14, 1);
}
