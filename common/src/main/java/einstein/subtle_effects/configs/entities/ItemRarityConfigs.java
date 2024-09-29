package einstein.subtle_effects.configs.entities;

import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import org.jetbrains.annotations.NotNull;

import static einstein.subtle_effects.init.ModConfigs.BASE_KEY;

public class ItemRarityConfigs extends ConfigSection {

    public DisplayType particlesDisplayType = DisplayType.ON;
    public ColorType particleColor = ColorType.NAME_COLOR;
    @ValidatedInt.Restrict(min = 3, max = 15)
    public int particleMaxHeight = 7;

    public enum DisplayType implements EnumTranslatable {
        OFF,
        ON,
        NOT_COMMON;

        @NotNull
        @Override
        public String prefix() {
            return BASE_KEY + "entities.itemRarity.particlesDisplayType";
        }
    }

    public enum ColorType implements EnumTranslatable {
        RARITY_COLOR,
        NAME_COLOR;

        @NotNull
        @Override
        public String prefix() {
            return BASE_KEY + "entities.itemRarity.particleColor";
        }
    }
}
