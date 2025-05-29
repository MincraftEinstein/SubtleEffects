package einstein.subtle_effects.configs.blocks;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedRegistryType;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

@Translation(prefix = ModConfigs.BASE_KEY + "blocks.fallingBlocks")
public class FallingBlocksConfigs extends ConfigSection {

    private static final List<Block> DEFAULT_DUSTY_BLOCKS = Util.make(new ArrayList<>(), blocks -> {
        blocks.add(Blocks.SAND);
        blocks.add(Blocks.SUSPICIOUS_SAND);
        blocks.add(Blocks.RED_SAND);
        blocks.add(Blocks.GRAVEL);
        blocks.add(Blocks.SUSPICIOUS_GRAVEL);

        for (DyeColor color : DyeColor.values()) {
            blocks.add(BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(color.getName() + "_concrete_powder")));
        }
    });

    public ValidatedList<Block> dustyBlocks = new ValidatedList<>(DEFAULT_DUSTY_BLOCKS, ValidatedRegistryType.of(BuiltInRegistries.BLOCK));
    public boolean fallingDust = true;
    public ValidatedInt fallingDustStartDistance = new ValidatedInt(3, 20, 0);
    public boolean landDust = true;
    public boolean landSound = true;
}
