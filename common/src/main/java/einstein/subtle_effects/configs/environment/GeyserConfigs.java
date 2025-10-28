package einstein.subtle_effects.configs.environment;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedRegistryType;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

@Translation(prefix = ModConfigs.BASE_KEY + "environment.geysers")
public class GeyserConfigs extends ConfigSection {

    private static final List<Block> NETHER_GEYSER_BLOCKS = List.of(Blocks.NETHERRACK, Blocks.BLACKSTONE, Blocks.SOUL_SOIL);
    private static final List<Block> BUBBLE_GEYSER_BLOCKS = List.of(Blocks.SAND, Blocks.RED_SAND, Blocks.GRAVEL, Blocks.DIRT, Blocks.CLAY);

    public boolean useUpdatedSmoke = false;
    public ConfigGroup flameGeysersGroup = new ConfigGroup("flame_geysers");
    public ValidatedInt flameGeyserSpawnChance = new ValidatedInt(5, 50, 0);
    public ValidatedList<Block> flameGeyserSpawnableBlocks = ValidatedRegistryType.of(BuiltInRegistries.BLOCK).toList(NETHER_GEYSER_BLOCKS);
    public ValidatedFloat flameGeyserSoundVolume = new ValidatedFloat(0.5F, 1, 0);
    public ValidatedInt flameGeyserActiveTime = new ValidatedInt(300, 1000, 50);
    @ConfigGroup.Pop
    public ValidatedInt flameGeyserInactiveTime = new ValidatedInt(500, 1000, 50);

    public ConfigGroup smokeGeysersGroup = new ConfigGroup("smoke_geysers");
    public ValidatedInt smokeGeyserSpawnChance = new ValidatedInt(5, 50, 0);
    public ValidatedList<Block> smokeGeyserSpawnableBlocks = ValidatedRegistryType.of(BuiltInRegistries.BLOCK).toList(NETHER_GEYSER_BLOCKS);
    public ValidatedFloat smokeGeyserSoundVolume = new ValidatedFloat(0.2F, 1, 0);
    public ValidatedInt smokeGeyserActiveTime = new ValidatedInt(300, 1000, 50);
    @ConfigGroup.Pop
    public ValidatedInt smokeGeyserInactiveTime = new ValidatedInt(500, 1000, 50);

    public ConfigGroup bubbleGeysersGroup = new ConfigGroup("bubble_geysers");
    public ValidatedInt bubbleGeyserSpawnChance = new ValidatedInt(5, 50, 0);
    public ValidatedList<Block> bubbleGeyserSpawnableBlocks = ValidatedRegistryType.of(BuiltInRegistries.BLOCK).toList(BUBBLE_GEYSER_BLOCKS);
    public ValidatedFloat bubbleGeyserSoundVolume = new ValidatedFloat(0.5F, 1, 0);
    public ValidatedInt bubbleGeyserActiveTime = new ValidatedInt(300, 1000, 50);
    @ConfigGroup.Pop
    public ValidatedInt bubbleGeyserInactiveTime = new ValidatedInt(500, 1000, 50);
}
