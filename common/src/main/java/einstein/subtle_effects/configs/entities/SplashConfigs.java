package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedRegistryType;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.List;

import static einstein.subtle_effects.init.ModConfigs.conditional;
import static einstein.subtle_effects.init.ModConfigs.createFailMessage;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.splashes")
public class SplashConfigs extends ConfigSection {

    public ValidatedBoolean splashEffects = new ValidatedBoolean();
    public ValidatedCondition<Boolean> splashRipples = conditional(new ValidatedBoolean(), splashEffects);
    public ValidatedCondition<Boolean> splashDroplets = conditional(new ValidatedBoolean(), splashEffects);
    public ValidatedCondition<Boolean> splashDropletSounds = conditional(new ValidatedBoolean(), splashEffects);
    public ValidatedCondition<Boolean> waterSplashBubbles = conditional(new ValidatedBoolean(), splashEffects);
    public ValidatedCondition<Boolean> explosionsCauseSplashes = conditional(new ValidatedBoolean(), splashEffects);
    public ValidatedCondition<Float> splashOverlayTint = conditional(new ValidatedFloat(0.2F, 1, 0), splashEffects);
    public ValidatedCondition<Float> splashOverlayAlpha = conditional(new ValidatedFloat(0.8F, 1, 0), splashEffects);
    public ValidatedCondition<Float> splashVelocityThreshold = conditional(new ValidatedFloat(0.35F, 1, 0), splashEffects);
    public ValidatedCondition<List<? extends EntityType<?>>> entityBlocklist = conditional(ValidatedRegistryType.of(BuiltInRegistries.ENTITY_TYPE).toList(List.of()), splashEffects);

    public ConfigGroup secondarySplashesGroup = new ConfigGroup("secondary_splashes");
    public ValidatedCondition<Boolean> secondarySplash = conditional(new ValidatedBoolean(), splashEffects);
    public ValidatedCondition<Boolean> secondarySplashRipples = conditional(new ValidatedBoolean(), splashEffects)
            .withCondition(secondarySplash, createFailMessage(() -> secondarySplash, true));
    @ConfigGroup.Pop
    public ValidatedCondition<Float> secondarySplashVelocityThreshold = conditional(new ValidatedFloat(0.3F, 1, 0), splashEffects)
            .withCondition(secondarySplash, createFailMessage(() -> secondarySplash, true));
}
