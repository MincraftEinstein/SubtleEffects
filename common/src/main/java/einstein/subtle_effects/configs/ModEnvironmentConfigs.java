package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.SubtleEffectsClient;
import einstein.subtle_effects.configs.environment.BiomeConfigs;
import einstein.subtle_effects.configs.environment.FireflyConfigs;
import einstein.subtle_effects.configs.environment.GeyserConfigs;
import einstein.subtle_effects.configs.environment.WaterfallConfigs;
import einstein.subtle_effects.init.ModBlockTickers;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;

import static einstein.subtle_effects.init.ModConfigs.restoreDefault;

@Version(version = 1)
@Translation(prefix = ModConfigs.BASE_KEY + "environment")
public class ModEnvironmentConfigs extends Config {

    public boolean biomeColorRain = true;
    public BiomeConfigs biomes = new BiomeConfigs();
    public GeyserConfigs geysers = new GeyserConfigs();
    public FireflyConfigs fireflies = new FireflyConfigs();
    public WaterfallConfigs waterfalls = new WaterfallConfigs();

    public ModEnvironmentConfigs() {
        super(SubtleEffects.loc("environment"));
    }

    @Override
    public void onUpdateClient() {
        ModBlockTickers.init();
        SubtleEffectsClient.clear();
    }

    @Override
    public void update(int deserializedVersion) {
        if (deserializedVersion == 0) {
            restoreDefault(biomes.mushroomSporeDensity);
            restoreDefault(biomes.pollenDensity);
            restoreDefault(biomes.sculkDustDensity);
        }
    }
}
