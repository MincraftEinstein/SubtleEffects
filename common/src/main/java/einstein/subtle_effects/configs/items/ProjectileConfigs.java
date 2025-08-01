package einstein.subtle_effects.configs.items;

import einstein.subtle_effects.configs.ReplacedParticlesDisplayType;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

@Translation(prefix = ModConfigs.BASE_KEY + "items.projectiles")
public class ProjectileConfigs extends ConfigSection {

    public boolean enderPearlTrail = true;
    public ReplacedParticlesDisplayType xpBottleParticlesDisplayType = ReplacedParticlesDisplayType.DEFAULT;
    public ValidatedInt xpBottleParticlesDensity = new ValidatedInt(10, 200, 5);
    public ValidatedFloat eggSmashSoundVolume = new ValidatedFloat(0.5F, 1, 0);
    public boolean eggSplatParticles = true;
    public ConfigGroup snowballGroup = new ConfigGroup("snowball");
    public ValidatedDouble snowballTrailDensity = new ValidatedDouble(0.5, 1, 0);
    public boolean snowballPoofsHaveSnowflakes = true;
    @ConfigGroup.Pop
    public ValidatedFloat snowballPoofSoundVolume = new ValidatedFloat(0.3F, 1, 0);
}
