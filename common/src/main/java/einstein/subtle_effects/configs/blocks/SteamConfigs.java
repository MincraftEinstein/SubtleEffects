package einstein.subtle_effects.configs.blocks;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import org.jetbrains.annotations.NotNull;

@Translation(prefix = ModConfigs.BASE_KEY + "blocks.steam")
public class SteamConfigs extends ConfigSection {

    public boolean lavaFizzSteam = true;
    public boolean replaceCampfireFoodSmoke = true;
    public boolean spongeDryingOutSteam = true;
    public SteamSpawnLogicType spawnLogic = SteamSpawnLogicType.BRIGHTNESS;
    public boolean steamingWater = false;
    public boolean boilingWater = false;
    public boolean steamingWaterCauldron = false;
    public boolean boilingWaterCauldron = false;
    public ValidatedInt steamingThreshold = new ValidatedInt(11, 14, 1);
    public ValidatedInt boilingThreshold = new ValidatedInt(13, 14, 1);

    public enum SteamSpawnLogicType implements EnumTranslatable {
        BRIGHTNESS,
        NEAR_LAVA;

        @Override
        public @NotNull String prefix() {
            return ModConfigs.BASE_KEY + "blocks.steam.spawnLogic";
        }
    }
}
