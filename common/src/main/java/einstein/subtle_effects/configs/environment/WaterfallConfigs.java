package einstein.subtle_effects.configs.environment;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

import static einstein.subtle_effects.init.ModConfigs.conditional;
import static einstein.subtle_effects.init.ModConfigs.createFailMessage;

@Translation(prefix = ModConfigs.BASE_KEY + "environment.waterfalls")
public class WaterfallConfigs extends ConfigSection {

    public ValidatedBoolean waterfallsEnabled = new ValidatedBoolean();
    public ValidatedCondition<Boolean> smallWaterfallsEnabled = conditional(new ValidatedBoolean(), waterfallsEnabled);
    public ValidatedCondition<Integer> waterfallUpdateFrequency = conditional(new ValidatedInt(6, 40, 1), waterfallsEnabled);
    public ValidatedCondition<Boolean> randomizeWaterfallParticleRotation = conditional(new ValidatedBoolean(false), waterfallsEnabled);

    public ConfigGroup mediumWaterfallsGroup = new ConfigGroup("medium_waterfalls");
    public ValidatedCondition<Float> mediumWaterfallParticleDensity = conditional(new ValidatedFloat(1, 1, 0.1F), waterfallsEnabled);
    public ValidatedCondition<Integer> mediumWaterfallHeightThreshold = conditional(new ValidatedInt(6, 20, 2), waterfallsEnabled);
    @ConfigGroup.Pop
    public ValidatedCondition<Boolean> forceSpawnMediumWaterfallParticles = conditional(new ValidatedBoolean(), waterfallsEnabled);

    public ConfigGroup largeWaterfallsGroup = new ConfigGroup("large_waterfalls");
    public ValidatedCondition<Boolean> largeWaterfallsEnabled = conditional(new ValidatedBoolean(), waterfallsEnabled);
    public ValidatedCondition<Float> largeWaterfallParticleDensity = conditional(new ValidatedFloat(1, 1, 0.1F), waterfallsEnabled)
            .withCondition(largeWaterfallsEnabled, createFailMessage(() -> largeWaterfallsEnabled, true));
    public ValidatedCondition<Integer> largeWaterfallHeightThreshold = conditional(new ValidatedInt(10, 20, 4), waterfallsEnabled)
            .withCondition(largeWaterfallsEnabled, createFailMessage(() -> largeWaterfallsEnabled, true));
    @ConfigGroup.Pop
    public ValidatedCondition<Boolean> forceSpawnLargeWaterfallParticles = conditional(new ValidatedBoolean(), waterfallsEnabled)
            .withCondition(largeWaterfallsEnabled, createFailMessage(() -> largeWaterfallsEnabled, true));
}
