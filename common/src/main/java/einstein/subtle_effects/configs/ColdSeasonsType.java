package einstein.subtle_effects.configs;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import org.jetbrains.annotations.NotNull;

public enum ColdSeasonsType implements EnumTranslatable {
    OFF,
    DEFAULT,
    WINTER_ONLY;

    @Override
    public @NotNull String prefix() {
        return ModConfigs.BASE_KEY + "coldSeasonsType";
    }
}
