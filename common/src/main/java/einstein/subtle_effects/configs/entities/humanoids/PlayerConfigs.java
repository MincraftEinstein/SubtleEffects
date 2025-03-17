package einstein.subtle_effects.configs.entities.humanoids;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.humanoids.player")
public class PlayerConfigs extends ConfigSection {

    public ConfigGroup stomachGrowlingGroup = new ConfigGroup("stomach_growling");
    public ValidatedInt stomachGrowlingThreshold = new ValidatedInt(6, 20, 0);
    public ValidatedFloat stomachGrowlingVolume = new ValidatedFloat(1, 1, 0);
    @ConfigGroup.Pop
    public ValidatedInt stomachGrowlingWaitTime = new ValidatedInt(15, 30, 5);

    public ConfigGroup heartBeatingGroup = new ConfigGroup("heart_beating");
    public ValidatedInt heartBeatingThreshold = new ValidatedInt(6, 20, 0);
    public ValidatedFloat heartbeatVolume = new ValidatedFloat(1, 1, 0);
    @ConfigGroup.Pop
    public ValidatedInt heartBeatingWaitTime = new ValidatedInt(3, 10, 2);
}
