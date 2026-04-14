package einstein.subtle_effects.configs.entities.humanoids;

import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.configs.ColdSeasonsType;
import einstein.subtle_effects.configs.entities.PerspectiveDisplayType;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

import static einstein.subtle_effects.init.ModConfigs.conditional;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.humanoids.frostyBreath")
public class FrostyBreathConfigs extends ConfigSection {

    public ValidatedEnum<PerspectiveDisplayType> displayType = new ValidatedEnum<>(PerspectiveDisplayType.DEFAULT);
    public ValidatedCondition<Float> alpha = conditional(new ValidatedFloat(0.5F, 1, 0.2F, ValidatedNumber.WidgetType.SLIDER),
            displayType, PerspectiveDisplayType.OFF);
    public ValidatedCondition<Integer> waitTime = conditional(new ValidatedInt(60, 200, 10),
            displayType, PerspectiveDisplayType.OFF);
    public ValidatedCondition<ColdSeasonsType> seasons = conditional(new ValidatedEnum<>(ColdSeasonsType.DEFAULT), CompatHelper.SERENESEASONS_MOD_ID)
            .withCondition(() -> displayType.get() != PerspectiveDisplayType.OFF, ModConfigs.createFailMessage(() -> displayType, false));
    public ValidatedCondition<List<? extends ResourceLocation>> additionalBiomes = conditional(ModConfigs.biomeList(), displayType, PerspectiveDisplayType.OFF);
}
