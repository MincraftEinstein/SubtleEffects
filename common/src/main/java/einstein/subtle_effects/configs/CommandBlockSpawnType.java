package einstein.subtle_effects.configs;

import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import org.jetbrains.annotations.NotNull;

import static einstein.subtle_effects.init.ModConfigs.BASE_KEY;

public enum CommandBlockSpawnType implements EnumTranslatable {
    ON,
    OFF,
    NOT_CREATIVE;

    @NotNull
    @Override
    public String prefix() {
        return BASE_KEY + "commandBlockType";
    }
}
