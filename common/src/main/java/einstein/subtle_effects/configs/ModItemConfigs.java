package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.SubtleEffectsClient;
import einstein.subtle_effects.configs.items.ItemRarityConfigs;
import einstein.subtle_effects.configs.items.ProjectileConfigs;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import net.minecraft.client.Minecraft;

@Translation(prefix = ModConfigs.BASE_KEY + "items")
public class ModItemConfigs extends Config {

    public ItemRarityConfigs itemRarity = new ItemRarityConfigs();
    public ProjectileConfigs projectiles = new ProjectileConfigs();

    public ConfigGroup axeGroup = new ConfigGroup("axe");
    public boolean axeStripParticles = true;
    public ReplacedParticlesDisplayType axeScrapeParticlesDisplayType = ReplacedParticlesDisplayType.DEFAULT;
    @ConfigGroup.Pop
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
    public boolean lingeringPotionClouds = true;
    public boolean splashPotionClouds = true;
    @RequiresAction(action = Action.RESTART)
    public boolean structureVoidItemMarker = true;
    public boolean armadilloBrushParticles = true;

    public ModItemConfigs() {
        super(SubtleEffects.loc("items"));
    }

    @Override
    public void onUpdateClient() {
        SubtleEffectsClient.clear(Minecraft.getInstance().level);
    }
}
