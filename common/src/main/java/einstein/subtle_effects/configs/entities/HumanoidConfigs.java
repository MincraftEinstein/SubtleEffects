package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.configs.ModEntityConfigs;
import einstein.subtle_effects.configs.entities.humanoids.FrostyBreathConfigs;
import einstein.subtle_effects.configs.entities.humanoids.PlayerConfigs;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import org.jetbrains.annotations.NotNull;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.humanoids")
public class HumanoidConfigs extends ConfigSection {

    public FrostyBreathConfigs frostyBreath = new FrostyBreathConfigs();
    public PlayerConfigs player = new PlayerConfigs();
    public ConfigGroup drowningBubblesGroup = new ConfigGroup("drowning_bubbles");
    public ModEntityConfigs.PerspectiveDisplayType drowningBubblesDisplayType = ModEntityConfigs.PerspectiveDisplayType.DEFAULT;
    public ValidatedInt drowningBubblesDensity = new ValidatedInt(3, 10, 1);
    @ConfigGroup.Pop
    public ValidatedFloat drowningBubbleAlpha = new ValidatedFloat(1, 1, 0.2F);
    public ConfigGroup potionRingsGroup = new ConfigGroup("potion_rings");
    public ModEntityConfigs.PerspectiveDisplayType potionRingsDisplayType = ModEntityConfigs.PerspectiveDisplayType.DEFAULT;
    public PotionRingsParticleType potionRingsParticleType = PotionRingsParticleType.BOTH;
    @ConfigGroup.Pop
    public boolean NPCsHavePotionRings = true;

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
