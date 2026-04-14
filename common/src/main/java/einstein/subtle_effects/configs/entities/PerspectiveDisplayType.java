package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public enum PerspectiveDisplayType implements EnumTranslatable {
    OFF,
    DEFAULT,
    THIRD_PERSON_ONLY;

    public boolean isEnabled() {
        return this != OFF;
    }

    public boolean test(Minecraft minecraft) {
        return this == DEFAULT || (this == THIRD_PERSON_ONLY && !minecraft.options.getCameraType().isFirstPerson());
    }

    @NotNull
    @Override
    public String prefix() {
        return ModConfigs.BASE_KEY + "entities.perspectiveDisplayType";
    }
}
