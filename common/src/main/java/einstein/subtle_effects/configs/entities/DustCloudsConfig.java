package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.dustClouds")
public class DustCloudsConfig extends ConfigSection {

    public ValidatedFloat scale = new ValidatedFloat(2, 2, 0.5F);
    public ValidatedFloat alpha = new ValidatedFloat(1, 1, 0.3F);
    public boolean preventWhenRaining = false;
    public boolean flyIntoWall = true;
    public boolean lessViewBlocking = false;

    public ConfigGroup fallingGroup = new ConfigGroup("falling");
    public boolean playerFell = true;
    public boolean mobFell = true;
    @ConfigGroup.Pop
    public boolean landMaceAttack = true;

    public ConfigGroup runningGroup = new ConfigGroup("running");
    public boolean playerRunning = true;
    public boolean mobRunning = true;
    @ConfigGroup.Pop
    public boolean playerRunningRequiresSpeed = false;
}
