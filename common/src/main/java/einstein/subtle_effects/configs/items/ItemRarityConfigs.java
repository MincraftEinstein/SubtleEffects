package einstein.subtle_effects.configs.items;

import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIngredient;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

import static einstein.subtle_effects.init.ModConfigs.BASE_KEY;
import static einstein.subtle_effects.init.ModConfigs.conditional;

@Translation(prefix = ModConfigs.BASE_KEY + "items.itemRarity")
public class ItemRarityConfigs extends ConfigSection {

    public ValidatedEnum<DisplayType> particlesDisplayType = new ValidatedEnum<>(DisplayType.ON);
    public ValidatedCondition<ParticleColorType> particleColorType = conditional(new ValidatedEnum<>(ParticleColorType.NAME_COLOR),
            particlesDisplayType, DisplayType.OFF);
    public ValidatedCondition<Boolean> mixedColorName = conditional(new ValidatedBoolean(), () -> {
        if (particlesDisplayType.get() != DisplayType.OFF) {
            return particleColorType.get() != ParticleColorType.RARITY_COLOR;
        }
        return false;
    }, () -> {
        if (particlesDisplayType.get() != DisplayType.OFF) {
            return particleColorType;
        }
        return particlesDisplayType;
    }, false);
    public ValidatedCondition<Boolean> useItemBorder = conditional(new ValidatedBoolean(), CompatHelper.ITEMBORDERS_MOD_ID)
            .withCondition(() -> particlesDisplayType.get() != DisplayType.OFF, ModConfigs.createFailMessage(() -> particlesDisplayType, false));
    public ValidatedCondition<Map<ValidatedIngredient.IngredientProvider, ? extends ValidatedColor.ColorHolder>> colorOverrides =
            conditional(new ValidatedMap<>(Map.of(), new ValidatedIngredient(Set.of()), new ValidatedColor(false)),
                    particlesDisplayType, DisplayType.OFF);
    public ValidatedCondition<Float> particleMaxHeight = conditional(new ValidatedFloat(1, 1.5F, 0.5F),
            particlesDisplayType, DisplayType.OFF);
    public ValidatedCondition<Float> particleMaxSpeed = conditional(new ValidatedFloat(1, 2, 0.1F),
            particlesDisplayType, DisplayType.OFF);
    public ValidatedCondition<Double> particleDensity = conditional(new ValidatedDouble(1, 1, 0.1),
            particlesDisplayType, DisplayType.OFF);

    public enum DisplayType implements EnumTranslatable {
        OFF,
        ON,
        NOT_COMMON;

        @NotNull
        @Override
        public String prefix() {
            return BASE_KEY + "items.itemRarity.particlesDisplayType";
        }
    }

    public enum ParticleColorType implements EnumTranslatable {
        RARITY_COLOR,
        NAME_COLOR,
        ONLY_COLOR_OVERRIDES;

        @NotNull
        @Override
        public String prefix() {
            return BASE_KEY + "items.itemRarity.particleColorType";
        }
    }
}
