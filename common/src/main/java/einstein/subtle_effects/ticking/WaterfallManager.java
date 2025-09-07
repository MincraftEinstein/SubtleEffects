package einstein.subtle_effects.ticking;

import einstein.subtle_effects.ticking.tickers.WaterfallTicker;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;

import java.util.HashMap;
import java.util.Map;

public class WaterfallManager {

    public static final Map<BlockPos, WaterfallTicker> WATERFALLS = new HashMap<>();

    public static void tick(Level level, FluidState fluidState, BlockPos pos) {
        if (fluidState.is(FluidTags.WATER) && fluidState.isSource()) {
            WaterfallTicker.trySpawn(level, pos);
        }
    }
}
