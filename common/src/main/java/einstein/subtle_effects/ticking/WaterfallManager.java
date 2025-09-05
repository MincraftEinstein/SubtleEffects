package einstein.subtle_effects.ticking;

import einstein.subtle_effects.ticking.tickers.WaterfallTicker;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterfallManager {

    public static final Map<BlockPos, WaterfallTicker> WATERFALLS = new HashMap<>();

    public static void tick(Level level, FluidState fluidState, BlockPos pos) {
        if (fluidState.is(FluidTags.WATER) && fluidState.isSource()) {
            WaterfallTicker.trySpawn(level, fluidState, pos);
        }
    }
}
