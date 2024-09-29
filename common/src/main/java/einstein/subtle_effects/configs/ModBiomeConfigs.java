package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.biome_particles.BiomeParticleManager;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

public class ModBiomeConfigs extends Config {

    @ValidatedInt.Restrict(min = 0, max = 32)
    public int biomeParticlesRadius = 32;
    public ValidatedList<ResourceLocation> mushroomSporeBiomes = biomeList("mushroom_fields");
    @ValidatedInt.Restrict(min = 0, max = 100)
    public int mushroomSporeDensity = 10;
    public ValidatedList<ResourceLocation> fireflyBiomes = biomeList("swamp", "mangrove_swamp");
    @ValidatedInt.Restrict(min = 0, max = 100)
    public int fireflyDensity = 6;
    public ValidatedList<ResourceLocation> pollenBiomes = biomeList("flower_forest", "sunflower_plains");
    @ValidatedInt.Restrict(min = 0, max = 100)
    public int pollenDensity = 50;
    public ValidatedList<ResourceLocation> sculkDustBiomes = biomeList("deep_dark");
    @ValidatedInt.Restrict(min = 0, max = 100)
    public int sculkDustDensity = 5;

    public ModBiomeConfigs() {
        super(SubtleEffects.loc("biomes"));
    }

    @Override
    public void onUpdateClient() {
        BiomeParticleManager.clear();
    }

    private static ValidatedList<ResourceLocation> biomeList(String... biomeIds) {
        return new ValidatedIdentifier().toList(Arrays.stream(biomeIds).map(ResourceLocation::withDefaultNamespace).toList());
    }
}
