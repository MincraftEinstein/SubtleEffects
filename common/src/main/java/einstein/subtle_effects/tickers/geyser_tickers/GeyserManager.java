package einstein.subtle_effects.tickers.geyser_tickers;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class GeyserManager {

    public static final Map<GeyserType, List<BlockPos>> ACTIVE_GEYSERS = new EnumMap<>(GeyserType.class);
    public static final Map<GeyserType, List<BlockPos>> INACTIVE_GEYSERS = new EnumMap<>(GeyserType.class);
    public static final List<Block> VALID_BLOCKS = List.of(Blocks.NETHERRACK, Blocks.BLACKSTONE, Blocks.SOUL_SOIL);

    public static void tick(Level level, BlockState state, BlockPos pos) {
        for (GeyserType type : GeyserType.values()) {
            if (type.spawnableBlocks.contains(state.getBlock())) {
                if (type.isNetherOnly && !level.dimension().equals(Level.NETHER)) {
                    return;
                }

                RandomSource random = RandomSource.create(state.getSeed(pos) + type.ordinal());
                if (random.nextDouble() < (0.0001 * type.spawnChance.get())) {
                    GeyserTicker.trySpawn(type, level, pos, random);
                }
            }
        }
    }
}
