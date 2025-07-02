package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.configs.environment.BiomeConfigs;
import einstein.subtle_effects.init.ModBlockTickers;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

@Translation(prefix = ModConfigs.BASE_KEY + "environment")
public class ModEnvironmentConfigs extends Config {

    public boolean biomeColorRain = true;
    public BiomeConfigs biomes = new BiomeConfigs();
    public ConfigGroup flameGeysersGroup = new ConfigGroup("flame_geysers");
    public ValidatedInt flameGeyserSpawnChance = new ValidatedInt(0, 50, 0);
    public ValidatedFloat flameGeyserSoundVolume = new ValidatedFloat(0.5F, 1, 0);
    public ValidatedInt flameGeyserActiveTime = new ValidatedInt(300, 1000, 50);
    @ConfigGroup.Pop
    public ValidatedInt flameGeyserInactiveTime = new ValidatedInt(500, 1000, 50);

    public ConfigGroup smokeGeysersGroup = new ConfigGroup("smoke_geysers");
    public ValidatedInt smokeGeyserSpawnChance = new ValidatedInt(0, 50, 0);
    public ValidatedFloat smokeGeyserSoundVolume = new ValidatedFloat(0.2F, 1, 0);
    public ValidatedInt smokeGeyserActiveTime = new ValidatedInt(300, 1000, 50);
    public ValidatedInt smokeGeyserInactiveTime = new ValidatedInt(500, 1000, 50);
    @ConfigGroup.Pop
    public boolean useUpdatedSmoke = false;

    public ConfigGroup bubbleGeysersGroup = new ConfigGroup("bubble_geysers");
    public ValidatedInt bubbleGeyserSpawnChance = new ValidatedInt(0, 50, 0);
    public ValidatedFloat bubbleGeyserSoundVolume = new ValidatedFloat(0.5F, 1, 0);
    public ValidatedInt bubbleGeyserActiveTime = new ValidatedInt(300, 1000, 50);
    @ConfigGroup.Pop
    public ValidatedInt bubbleGeyserInactiveTime = new ValidatedInt(500, 1000, 50);

    public ModEnvironmentConfigs() {
        super(SubtleEffects.loc("environment"));
    }

    @Override
    public void onUpdateClient() {
        BiomeParticleManager.clear();
        ModBlockTickers.init();
    }
}
