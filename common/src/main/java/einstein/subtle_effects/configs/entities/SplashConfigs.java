package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedRegistryType;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.List;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.splashes")
public class SplashConfigs extends ConfigSection {

    public boolean splashEffects = true;
    public boolean splashRipples = true;
    public boolean splashDroplets = true;
    public boolean splashBubbles = true;
    public ValidatedFloat splashOverlayTint = new ValidatedFloat(0.2F, 1, 0);
    public ValidatedFloat splashOverlayAlpha = new ValidatedFloat(0.8F, 1, 0);
    public ValidatedFloat splashVelocityThreshold = new ValidatedFloat(0.3F, 1, 0);
    public ValidatedList<EntityType<?>> entityBlocklist = new ValidatedList<>(List.of(), ValidatedRegistryType.of(BuiltInRegistries.ENTITY_TYPE));
    public ConfigGroup secondarySplashesGroup = new ConfigGroup("secondary_splashes");
    public boolean secondarySplash = true;
    public boolean secondarySplashRipples = false;
    @ConfigGroup.Pop
    public ValidatedFloat secondarySplashVelocityThreshold = new ValidatedFloat(0.3F, 1, 0);
    public ConfigGroup lavaSplashesGroup = new ConfigGroup("lava_splashes");
    public boolean lavaSplashes = true;
    @ConfigGroup.Pop
    public boolean lavaSplashRipples = true;
}
