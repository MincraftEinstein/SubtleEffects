package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.configs.SmokeType;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedRegistryType;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.List;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.burning")
public class BurningEntityConfigs extends ConfigSection {

    private static final List<EntityType<?>> DEFAULT_ENTITY_BLOCKLIST = List.of(EntityType.LIGHTNING_BOLT);

    public SmokeType smokeType = SmokeType.DEFAULT;
    public ValidatedFloat smokeDensity = new ValidatedFloat(1, 1, 0);
    public ValidatedFloat flamesDensity = new ValidatedFloat(1, 1, 0);
    public ValidatedFloat sparksDensity = new ValidatedFloat(1, 1, 0);
    public ValidatedFloat soundVolume = new ValidatedFloat(0.3F, 1, 0);
    public ValidatedList<EntityType<?>> entityBlocklist = ValidatedRegistryType.of(BuiltInRegistries.ENTITY_TYPE).toList(DEFAULT_ENTITY_BLOCKLIST);
    public boolean overrideDyedFlamesEffects = true;
    public boolean extinguishSteam = true;
}
