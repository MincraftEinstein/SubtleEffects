package einstein.subtle_effects.configs.environment;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedRegistryType;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
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
    public ValidatedCondition<List<? extends Block>> flameGeyserSpawnableBlocks =
            ModConfigs.conditional(ValidatedRegistryType.of(BuiltInRegistries.BLOCK).toList(NETHER_GEYSER_BLOCKS), flameGeyserSpawnChance);
    public ValidatedCondition<Float> flameGeyserSoundVolume = ModConfigs.conditional(new ValidatedFloat(0.5F, 1, 0), flameGeyserSpawnChance);
    public ValidatedCondition<Integer> flameGeyserActiveTime = ModConfigs.conditional(new ValidatedInt(300, 1000, 50), flameGeyserSpawnChance);
    @ConfigGroup.Pop
    public ValidatedCondition<Integer> flameGeyserInactiveTime = ModConfigs.conditional(new ValidatedInt(500, 1000, 50), flameGeyserSpawnChance);

    public ConfigGroup smokeGeysersGroup = new ConfigGroup("smoke_geysers");
    public ValidatedInt smokeGeyserSpawnChance = new ValidatedInt(5, 50, 0);
    public ValidatedCondition<List<? extends Block>> smokeGeyserSpawnableBlocks =
            ModConfigs.conditional(ValidatedRegistryType.of(BuiltInRegistries.BLOCK).toList(NETHER_GEYSER_BLOCKS), smokeGeyserSpawnChance);
    public ValidatedCondition<Float> smokeGeyserSoundVolume = ModConfigs.conditional(new ValidatedFloat(0.2F, 1, 0), smokeGeyserSpawnChance);
    public ValidatedCondition<Integer> smokeGeyserActiveTime = ModConfigs.conditional(new ValidatedInt(300, 1000, 50), smokeGeyserSpawnChance);
    @ConfigGroup.Pop
    public ValidatedCondition<Integer> smokeGeyserInactiveTime = ModConfigs.conditional(new ValidatedInt(500, 1000, 50), smokeGeyserSpawnChance);

    public ConfigGroup bubbleGeysersGroup = new ConfigGroup("bubble_geysers");
    public ValidatedInt bubbleGeyserSpawnChance = new ValidatedInt(5, 50, 0);
    public ValidatedCondition<List<? extends Block>> bubbleGeyserSpawnableBlocks =
            ModConfigs.conditional(ValidatedRegistryType.of(BuiltInRegistries.BLOCK).toList(BUBBLE_GEYSER_BLOCKS), bubbleGeyserSpawnChance);
    public ValidatedCondition<Float> bubbleGeyserSoundVolume = ModConfigs.conditional(new ValidatedFloat(0.5F, 1, 0), bubbleGeyserSpawnChance);
    public ValidatedCondition<Integer> bubbleGeyserActiveTime = ModConfigs.conditional(new ValidatedInt(300, 1000, 50), bubbleGeyserSpawnChance);
    @ConfigGroup.Pop
    public ValidatedCondition<Integer> bubbleGeyserInactiveTime = ModConfigs.conditional(new ValidatedInt(500, 1000, 50), bubbleGeyserSpawnChance);
}
