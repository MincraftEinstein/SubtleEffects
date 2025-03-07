package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;

@Translation(prefix = ModConfigs.BASE_KEY + "server")
public class ModServerConfigs extends Config {

    public boolean disableRegistrySyncing = true;

    public ModServerConfigs() {
        super(SubtleEffects.loc("server"));
    }
}
