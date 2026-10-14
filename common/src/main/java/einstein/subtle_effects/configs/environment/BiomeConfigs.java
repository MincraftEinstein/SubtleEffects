package einstein.subtle_effects.configs.environment;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.ValidatedField;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

import static einstein.subtle_effects.init.ModConfigs.biomeList;
import static einstein.subtle_effects.init.ModConfigs.conditional;

@Translation(prefix = ModConfigs.BASE_KEY + "environment.biomes")
public class BiomeConfigs extends ConfigSection {

    public ValidatedFloat mushroomSporeDensity = new ValidatedFloat(0.5F, 1, 0);
    public ValidatedCondition<List<? extends ResourceLocation>> mushroomSporeBiomes = conditionalBiomeList(mushroomSporeDensity, "mushroom_fields");
    public ValidatedFloat pollenDensity = new ValidatedFloat(0.75F, 1, 0);
    public ValidatedCondition<List<? extends ResourceLocation>> pollenBiomes = conditionalBiomeList(pollenDensity, "flower_forest", "sunflower_plains");
    public ValidatedFloat sculkDustDensity = new ValidatedFloat(0.25F, 1, 0);
    public ValidatedCondition<List<? extends ResourceLocation>> sculkDustBiomes = conditionalBiomeList(sculkDustDensity, "deep_dark");

    private ValidatedCondition<List<? extends ResourceLocation>> conditionalBiomeList(ValidatedField<Float> dependencyConfig, String... biomeIds) {
        return conditional(biomeList(biomeIds), () -> dependencyConfig.get() > 0, () -> dependencyConfig, false);
    }
}
