package einstein.subtle_effects.configs.entities.humanoids;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

import static einstein.subtle_effects.init.ModConfigs.conditional;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.humanoids.player")
public class PlayerConfigs extends ConfigSection {

    public boolean enableBreathingEffectsInCreative = true;
    public ConfigGroup stomachGrowlingGroup = new ConfigGroup("stomach_growling");
    public ValidatedInt stomachGrowlingThreshold = new ValidatedInt(6, 20, 0);
    public ValidatedCondition<Float> stomachGrowlingVolume = conditional(new ValidatedFloat(1, 1, 0), stomachGrowlingThreshold);
    @ConfigGroup.Pop
    public ValidatedCondition<Integer> stomachGrowlingWaitTime = conditional(new ValidatedInt(15, 30, 5), stomachGrowlingThreshold);

    public ConfigGroup heartBeatingGroup = new ConfigGroup("heart_beating");
    public ValidatedInt heartBeatingThreshold = new ValidatedInt(6, 20, 0);
    public ValidatedCondition<Float> heartbeatVolume = conditional(new ValidatedFloat(1, 1, 0), heartBeatingThreshold);
    @ConfigGroup.Pop
    public ValidatedCondition<Integer> heartBeatingWaitTime = conditional(new ValidatedInt(3, 10, 2), heartBeatingThreshold);
}
