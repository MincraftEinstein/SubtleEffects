package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.ticking.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.configs.environment.BiomeConfigs;
import einstein.subtle_effects.configs.environment.FireflyConfigs;
import einstein.subtle_effects.configs.environment.GeyserConfigs;
import einstein.subtle_effects.init.ModBlockTickers;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import net.minecraft.client.Minecraft;

@Translation(prefix = ModConfigs.BASE_KEY + "environment")
public class ModEnvironmentConfigs extends Config {

    public boolean biomeColorRain = true;
    public BiomeConfigs biomes = new BiomeConfigs();
    public GeyserConfigs geysers = new GeyserConfigs();
    public FireflyConfigs fireflies = new FireflyConfigs();

    public ModEnvironmentConfigs() {
        super(SubtleEffects.loc("environment"));
    }

    @Override
    public void onUpdateClient() {
        BiomeParticleManager.clear();
        ModBlockTickers.init();
        TickerManager.clear(Minecraft.getInstance().level);
    }
}
