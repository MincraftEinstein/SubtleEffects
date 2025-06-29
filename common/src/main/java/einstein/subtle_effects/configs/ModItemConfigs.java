package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.configs.items.ItemRarityConfigs;
import einstein.subtle_effects.configs.items.ProjectileConfigs;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;

@Translation(prefix = ModConfigs.BASE_KEY + "items")
public class ModItemConfigs extends Config {

    public ItemRarityConfigs itemRarity = new ItemRarityConfigs();
    public ProjectileConfigs projectiles = new ProjectileConfigs();
    public boolean axeStripParticles = true;
    public ReplacedParticlesDisplayType axeScrapeParticlesDisplayType = ReplacedParticlesDisplayType.DEFAULT;
    public ReplacedParticlesDisplayType axeWaxOffParticlesDisplayType = ReplacedParticlesDisplayType.DEFAULT;
    public boolean boneMealUsingParticles = true;
    public boolean flintAndSteelParticles = true;
    public boolean increasedItemBreakParticles = true;
    public ConfigGroup bucketsGroup = new ConfigGroup("buckets");
    public boolean waterEvaporateFromBucketSteam = true;
    public boolean waterBucketUseParticles = true;
    public boolean lavaBucketUseParticles = true;
    @ConfigGroup.Pop
    public boolean powderSnowBucketUseParticles = true;

    public ModItemConfigs() {
        super(SubtleEffects.loc("items"));
    }
}
