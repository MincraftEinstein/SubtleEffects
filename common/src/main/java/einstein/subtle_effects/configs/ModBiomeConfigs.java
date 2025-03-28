package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

@Translation(prefix = ModConfigs.BASE_KEY + "biomes")
public class ModBiomeConfigs extends Config {

    public boolean biomeColorRain = true;
    @ValidatedInt.Restrict(min = 0, max = 32)
    public int biomeParticlesRadius = 32;
    public ValidatedList<ResourceLocation> mushroomSporeBiomes = biomeList("mushroom_fields");
    public ValidatedInt mushroomSporeDensity = new ValidatedInt(10, 100, 0);
    public ValidatedList<ResourceLocation> fireflyBiomes = biomeList("swamp", "mangrove_swamp");
    public ValidatedInt fireflyDensity = new ValidatedInt(6, 100, 0);
    public ValidatedList<ResourceLocation> pollenBiomes = biomeList("flower_forest", "sunflower_plains");
    public ValidatedInt pollenDensity = new ValidatedInt(50, 100, 0);
    public ValidatedList<ResourceLocation> sculkDustBiomes = biomeList("deep_dark");
    public ValidatedInt sculkDustDensity = new ValidatedInt(5, 100, 0);

    public ModBiomeConfigs() {
        super(SubtleEffects.loc("biomes"));
    }

    @Override
    public void onUpdateClient() {
        BiomeParticleManager.clear();
    }

    public static ValidatedList<ResourceLocation> biomeList(String... biomeIds) {
        return new ValidatedList<>(Arrays.stream(biomeIds).map(ResourceLocation::withDefaultNamespace).toList(),
                ValidatedIdentifier.ofRegistryKey(Registries.BIOME, (location, biomeHolder) -> true)
        );
    }
}
