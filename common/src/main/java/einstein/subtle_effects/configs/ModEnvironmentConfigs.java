package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.configs.environment.BiomeConfigs;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;

@Translation(prefix = ModConfigs.BASE_KEY + "environment")
public class ModEnvironmentConfigs extends Config {

    public boolean biomeColorRain = true;
    public BiomeConfigs biomes = new BiomeConfigs();

    public ModEnvironmentConfigs() {
        super(SubtleEffects.loc("environment"));
    }

    @Override
    public void onUpdateClient() {
        BiomeParticleManager.clear();
    }
}
