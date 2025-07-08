package einstein.subtle_effects.configs.entities.humanoids;

import einstein.subtle_effects.configs.ModEntityConfigs;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.humanoids.frostyBreath")
public class FrostyBreathConfigs extends ConfigSection {

    public ModEntityConfigs.PerspectiveDisplayType displayType = ModEntityConfigs.PerspectiveDisplayType.DEFAULT;
    public ValidatedFloat alpha = new ValidatedFloat(0.5F, 1, 0.2F, ValidatedNumber.WidgetType.SLIDER);
    public ValidatedInt waitTime = new ValidatedInt(60, 200, 10);
    public Seasons seasons = Seasons.DEFAULT;
    public ValidatedList<ResourceLocation> additionalBiomes = ModConfigs.biomeList();

    public enum Seasons implements EnumTranslatable {
        OFF,
        DEFAULT,
        WINTER_ONLY;

        @Override
        public @NotNull String prefix() {
            return ModConfigs.BASE_KEY + "entities.humanoids.frostyBreath.seasons";
        }
    }
}
