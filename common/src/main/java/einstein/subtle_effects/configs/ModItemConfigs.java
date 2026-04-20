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
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import net.minecraft.client.Minecraft;

@Translation(prefix = ModConfigs.BASE_KEY + "items")
public class ModItemConfigs extends Config {

    public ItemRarityConfigs itemRarity = new ItemRarityConfigs();
    public ProjectileConfigs projectiles = new ProjectileConfigs();

    public ConfigGroup axeGroup = new ConfigGroup("axe");
    public ValidatedBoolean axeStripParticles = new ValidatedBoolean();
    public ValidatedCondition<ReplacedParticlesDisplayType> axeScrapeParticlesDisplayType
            = ModConfigs.conditional(new ValidatedEnum<>(ReplacedParticlesDisplayType.DEFAULT), axeStripParticles);
    @ConfigGroup.Pop
    public ValidatedCondition<ReplacedParticlesDisplayType> axeWaxOffParticlesDisplayType
            = ModConfigs.conditional(new ValidatedEnum<>(ReplacedParticlesDisplayType.DEFAULT), axeStripParticles);

    public boolean boneMealUsingParticles = true;
    public boolean flintAndSteelParticles = true;
    public boolean increasedItemBreakParticles = true;
    public ConfigGroup bucketsGroup = new ConfigGroup("buckets");
    public boolean waterEvaporateFromBucketSteam = true;
    public boolean fluidBucketUseParticles = true;
    public boolean powderSnowBucketUseParticles = true;
    @ConfigGroup.Pop
    public boolean powderSnowBucketBlockPlaceSound = true;
    public boolean lingeringPotionClouds = true;
    public boolean splashPotionClouds = true;
    @RequiresAction(action = Action.RESTART)
    public boolean structureVoidItemMarker = true;
    public boolean armadilloBrushParticles = true;
    public ValidatedFloat spawnEggUseSoundVolume = new ValidatedFloat(0.5F, 1, 0);
    public boolean spawnEggUseParticles = true;

    public ModItemConfigs() {
        super(SubtleEffects.loc("items"));
    }

    @Override
    public void onUpdateClient() {
        SubtleEffectsClient.clear(Minecraft.getInstance().level);
    }
}
