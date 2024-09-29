package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.config.Config;

public class ModItemConfigs extends Config {

    public boolean axeStripParticles = true;
    public boolean waterEvaporateFromBucketSteam = true;
    public boolean boneMealUsingParticles = true;

    public ModItemConfigs() {
        super(SubtleEffects.loc("items"));
    }
}
