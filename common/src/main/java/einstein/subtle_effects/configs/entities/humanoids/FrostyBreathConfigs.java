package einstein.subtle_effects.configs.entities.humanoids;

import einstein.subtle_effects.configs.ModEntityConfigs;
import einstein.subtle_effects.configs.ColdSeasonsType;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import net.minecraft.resources.ResourceLocation;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.humanoids.frostyBreath")
public class FrostyBreathConfigs extends ConfigSection {

    public ModEntityConfigs.PerspectiveDisplayType displayType = ModEntityConfigs.PerspectiveDisplayType.DEFAULT;
    public ValidatedFloat alpha = new ValidatedFloat(0.5F, 1, 0.2F, ValidatedNumber.WidgetType.SLIDER);
    public ValidatedInt waitTime = new ValidatedInt(60, 200, 10);
    public ColdSeasonsType seasons = ColdSeasonsType.DEFAULT;
    public ValidatedList<ResourceLocation> additionalBiomes = ModConfigs.biomeList();

}
