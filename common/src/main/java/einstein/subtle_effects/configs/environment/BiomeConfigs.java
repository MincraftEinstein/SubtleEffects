package einstein.subtle_effects.configs.environment;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.resources.Identifier;

import static einstein.subtle_effects.init.ModConfigs.biomeList;

@Translation(prefix = ModConfigs.BASE_KEY + "environment.biomes")
public class BiomeConfigs extends ConfigSection {

    public ValidatedInt biomeParticlesRadius = new ValidatedInt(32, 32, 0);
    public ValidatedList<Identifier> mushroomSporeBiomes = biomeList("mushroom_fields");
    public ValidatedInt mushroomSporeDensity = new ValidatedInt(10, 100, 0);
    public ValidatedList<Identifier> pollenBiomes = biomeList("flower_forest", "sunflower_plains");
    public ValidatedInt pollenDensity = new ValidatedInt(50, 100, 0);
    public ValidatedList<Identifier> sculkDustBiomes = biomeList("deep_dark");
    public ValidatedInt sculkDustDensity = new ValidatedInt(5, 100, 0);
}
