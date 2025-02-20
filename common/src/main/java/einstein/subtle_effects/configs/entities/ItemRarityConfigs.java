package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import org.jetbrains.annotations.NotNull;

import static einstein.subtle_effects.init.ModConfigs.BASE_KEY;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.itemRarity")
public class ItemRarityConfigs extends ConfigSection {

    public DisplayType particlesDisplayType = DisplayType.ON;
    public ParticleColorType particleColorType = ParticleColorType.NAME_COLOR;
    public boolean mixedColorName = true;
    public boolean useItemBorder = true;
    public ValidatedFloat particleMaxHeight = new ValidatedFloat(1, 1.5F, 0.5F);
    public ValidatedFloat particleMaxSpeed = new ValidatedFloat(1, 2, 0.1F);
    public ValidatedDouble particleDensity = new ValidatedDouble(1, 1, 0.1);

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

    public enum ParticleColorType implements EnumTranslatable {
        RARITY_COLOR,
        NAME_COLOR;

        @NotNull
        @Override
        public String prefix() {
            return BASE_KEY + "entities.itemRarity.particleColorType";
        }
    }
}
