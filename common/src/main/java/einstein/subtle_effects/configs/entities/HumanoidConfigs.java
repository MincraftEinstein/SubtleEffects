package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.configs.entities.humanoids.FrostyBreathConfigs;
import einstein.subtle_effects.configs.entities.humanoids.PlayerConfigs;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import org.jetbrains.annotations.NotNull;

import static einstein.subtle_effects.init.ModConfigs.conditional;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.humanoids")
public class HumanoidConfigs extends ConfigSection {

    public FrostyBreathConfigs frostyBreath = new FrostyBreathConfigs();
    public PlayerConfigs player = new PlayerConfigs();
    public ConfigGroup drowningBubblesGroup = new ConfigGroup("drowning_bubbles");
    public ValidatedEnum<PerspectiveDisplayType> drowningBubblesDisplayType = new ValidatedEnum<>(PerspectiveDisplayType.DEFAULT);
    public ValidatedCondition<Integer> drowningBubblesDensity = conditional(new ValidatedInt(3, 10, 1),
            drowningBubblesDisplayType, PerspectiveDisplayType.OFF);
    public ValidatedCondition<Float> drowningBubbleAlpha = conditional(new ValidatedFloat(1, 1, 0.2F),
            drowningBubblesDisplayType, PerspectiveDisplayType.OFF);
    @ConfigGroup.Pop
    public ValidatedCondition<Boolean> forceDrowningBubblesToColumn = conditional(new ValidatedBoolean(false),
            drowningBubblesDisplayType, PerspectiveDisplayType.OFF);

    public ConfigGroup potionRingsGroup = new ConfigGroup("potion_rings");
    public ValidatedEnum<PerspectiveDisplayType> potionRingsDisplayType = new ValidatedEnum<>(PerspectiveDisplayType.DEFAULT);
    public ValidatedCondition<PotionRingsParticleType> potionRingsParticleType = conditional(new ValidatedEnum<>(PotionRingsParticleType.BOTH),
            potionRingsDisplayType, PerspectiveDisplayType.OFF);
    public ValidatedCondition<Float> potionRingsScale = conditional(new ValidatedFloat(1, 1.5F, 1),
            potionRingsDisplayType, PerspectiveDisplayType.OFF);
    public ValidatedCondition<Float> potionRingsAlpha = conditional(new ValidatedFloat(1, 1, 0.2F),
            potionRingsDisplayType, PerspectiveDisplayType.OFF);
    @ConfigGroup.Pop
    public ValidatedCondition<Boolean> NPCsHavePotionRings = conditional(new ValidatedBoolean(),
            potionRingsDisplayType, PerspectiveDisplayType.OFF);

    public enum PotionRingsParticleType implements EnumTranslatable {
        RINGS_ONLY,
        DOTS_ONLY,
        BOTH;

        @Override
        public @NotNull String prefix() {
            return ModConfigs.BASE_KEY + "entities.humanoids.potionRingsParticleType";
        }
    }
}
