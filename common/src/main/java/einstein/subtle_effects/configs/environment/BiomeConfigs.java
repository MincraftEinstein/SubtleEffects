package einstein.subtle_effects.configs.environment;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.ValidatedField;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

import static einstein.subtle_effects.init.ModConfigs.biomeList;
import static einstein.subtle_effects.init.ModConfigs.conditional;

@Translation(prefix = ModConfigs.BASE_KEY + "environment.biomes")
public class BiomeConfigs extends ConfigSection {

    public ValidatedInt biomeParticlesRadius = new ValidatedInt(32, 32, 0);
    public ValidatedCondition<Integer> mushroomSporeDensity = conditional(new ValidatedInt(10, 100, 0), biomeParticlesRadius);
    public ValidatedCondition<List<? extends ResourceLocation>> mushroomSporeBiomes = conditionalBiomeList(mushroomSporeDensity, "mushroom_fields");
    public ValidatedCondition<Integer> pollenDensity = conditional(new ValidatedInt(50, 100, 0), biomeParticlesRadius);
    public ValidatedCondition<List<? extends ResourceLocation>> pollenBiomes = conditionalBiomeList(pollenDensity, "flower_forest", "sunflower_plains");
    public ValidatedCondition<Integer> sculkDustDensity = conditional(new ValidatedInt(5, 100, 0), biomeParticlesRadius);
    public ValidatedCondition<List<? extends ResourceLocation>> sculkDustBiomes = conditionalBiomeList(sculkDustDensity, "deep_dark");

    private ValidatedCondition<List<? extends ResourceLocation>> conditionalBiomeList(ValidatedField<Integer> dependencyConfig, String... biomeIds) {
        return conditional(biomeList(biomeIds),
                () -> biomeParticlesRadius.get() > 0 && dependencyConfig.get() > 0,
                () -> biomeParticlesRadius.get() == 0 ? biomeParticlesRadius : dependencyConfig, false);
    }
}
