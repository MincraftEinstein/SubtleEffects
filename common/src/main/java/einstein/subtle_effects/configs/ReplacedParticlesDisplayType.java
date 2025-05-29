package einstein.subtle_effects.configs;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import org.jetbrains.annotations.NotNull;

public enum ReplacedParticlesDisplayType implements EnumTranslatable {
    DEFAULT,
    VANILLA,
    BOTH;

    @Override
    public @NotNull String prefix() {
        return ModConfigs.BASE_KEY + "replacedParticlesDisplayType";
    }
}
