package einstein.subtle_effects.configs.environment;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

@Translation(prefix = ModConfigs.BASE_KEY + "environment.waterfalls")
public class WaterfallConfigs extends ConfigSection {

    public boolean waterfallsEnabled = true;
    public boolean smallWaterfallsEnabled = true;
    public ValidatedInt waterfallUpdateFrequency = new ValidatedInt(6, 40, 1);
    public boolean randomizeWaterfallParticleRotation = false;

    public ConfigGroup mediumWaterfallsGroup = new ConfigGroup("medium_waterfalls");
    public ValidatedFloat mediumWaterfallParticleDensity = new ValidatedFloat(1, 1, 0.1F);
    public ValidatedInt mediumWaterfallHeightThreshold = new ValidatedInt(6, 20, 2);
    @ConfigGroup.Pop
    public boolean forceSpawnMediumWaterfallParticles = true;

    public ConfigGroup largeWaterfallsGroup = new ConfigGroup("large_waterfalls");
    public boolean largeWaterfallsEnabled = true;
    public ValidatedFloat largeWaterfallParticleDensity = new ValidatedFloat(1, 1, 0.1F);
    public ValidatedInt largeWaterfallHeightThreshold = new ValidatedInt(10, 20, 4);
    @ConfigGroup.Pop
    public boolean forceSpawnLargeWaterfallParticles = true;
}
