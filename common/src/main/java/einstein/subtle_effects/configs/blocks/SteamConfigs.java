package einstein.subtle_effects.configs.blocks;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import org.jetbrains.annotations.NotNull;

@Translation(prefix = ModConfigs.BASE_KEY + "blocks.steam")
public class SteamConfigs extends ConfigSection {

    public boolean lavaFizzSteam = true;
    public boolean replaceCampfireFoodSmoke = true;
    public boolean spongeDryingOutSteam = true;
    public boolean replaceRainEvaporationSteam = true;
    public boolean lavaCauldronsEvaporateRain = true;
    public SteamSpawnLogicType spawnLogic = SteamSpawnLogicType.BRIGHTNESS;
    public ValidatedBoolean steamingWater = new ValidatedBoolean(false);
    public ValidatedBoolean boilingWater = new ValidatedBoolean(false);
    public ValidatedBoolean steamingWaterCauldron = new ValidatedBoolean(false);
    public ValidatedBoolean boilingWaterCauldron = new ValidatedBoolean(false);
    public ValidatedCondition<Integer> steamingThreshold = ModConfigs.conditional(new ValidatedInt(11, 14, 1),
            () -> steamingWater.get() || steamingWaterCauldron.get(), () -> steamingWater.get() ? steamingWater : steamingWaterCauldron, false);
    public ValidatedCondition<Integer> boilingThreshold = ModConfigs.conditional(new ValidatedInt(13, 14, 1),
            () -> boilingWater.get() || boilingWaterCauldron.get(), () -> boilingWater.get() ? boilingWater : boilingWaterCauldron, false);

    public enum SteamSpawnLogicType implements EnumTranslatable {
        BRIGHTNESS,
        NEAR_LAVA;

        @Override
        public @NotNull String prefix() {
            return ModConfigs.BASE_KEY + "blocks.steam.spawnLogic";
        }
    }
}
