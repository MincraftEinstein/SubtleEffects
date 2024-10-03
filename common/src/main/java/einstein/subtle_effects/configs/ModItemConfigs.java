package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;

@Translation(prefix = ModConfigs.BASE_KEY + "items")
public class ModItemConfigs extends Config {

    public boolean axeStripParticles = true;
    public boolean waterEvaporateFromBucketSteam = true;
    public boolean boneMealUsingParticles = true;

    public ModItemConfigs() {
        super(SubtleEffects.loc("items"));
    }
}
