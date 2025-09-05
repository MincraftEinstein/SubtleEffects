package einstein.subtle_effects.ticking.tickers;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static einstein.subtle_effects.ticking.WaterfallManager.WATERFALLS;
import static einstein.subtle_effects.ticking.tickers.WaterfallTicker.WaterfallData.EMPTY_WATERFALL_DATA;
import static net.minecraft.util.Mth.nextDouble;

public class WaterfallTicker extends Ticker {

    private static final int MAX_UPDATE_TICKS = 6;
    private final Level level;
    private final BlockPos pos;
    private final BlockPos waterfallPos;
    private final RandomSource random = RandomSource.create();
    private WaterfallData data;
    private int updateTicks;

    public WaterfallTicker(Level level, BlockPos pos, BlockPos waterfallPos, WaterfallData data) {
        this.level = level;
        this.pos = pos;
        this.waterfallPos = waterfallPos;
        this.data = data;
    }

    public static void trySpawn(Level level, FluidState fluidState, BlockPos pos) {
        if (!WATERFALLS.containsKey(pos)) {
            BlockPos waterfallPos = pos.above();
            FluidState waterfallState = level.getFluidState(waterfallPos);
            WaterfallData data = getWaterfallIntensity(level, fluidState, waterfallPos, waterfallState);

            if (!data.equals(EMPTY_WATERFALL_DATA)) {
                WaterfallTicker ticker = new WaterfallTicker(level, pos, waterfallPos, data);

                WATERFALLS.put(pos, ticker);
                TickerManager.add(ticker);
            }
        }
    }

    @Override
    public void tick() {
        FluidState fluidState = level.getFluidState(waterfallPos);
        Vec3 flow = fluidState.getFlow(level, waterfallPos);

        if (flow.equals(Vec3.ZERO)) {
            for (Direction openSide : data.openSides()) {
                flow.add(openSide.getStepX(), 0, openSide.getStepZ());
            }
        }

        for (Direction direction : data.openSides()) {
            Direction.Axis axis = direction.getAxis();
            double xOffset = axis == Direction.Axis.X ? 0.5 + (0.7 * direction.getStepX()) : random.nextFloat();
            double zOffset = axis == Direction.Axis.Z ? 0.5 + (0.7 * direction.getStepZ()) : random.nextFloat();

            level.addParticle(ModParticles.CASCADE.get(),
                    waterfallPos.getX() + xOffset,
                    waterfallPos.getY() + 0.2F,
                    waterfallPos.getZ() + zOffset,
                    nextDouble(random, 0.5, 1) * flow.x(),
                    0,
                    nextDouble(random, 0.5, 1) * flow.z()
            );
        }

        if (updateTicks++ >= MAX_UPDATE_TICKS) {
            WaterfallData newData = getWaterfallIntensity(level, level.getFluidState(pos), waterfallPos, fluidState);
            if (newData.equals(EMPTY_WATERFALL_DATA)) {
                remove();
                return;
            }

            data = newData;
            updateTicks = 0;
        }
    }

    @Override
    public void remove() {
        super.remove();
        WATERFALLS.remove(pos);
    }

    private static WaterfallData getWaterfallIntensity(Level level, FluidState fluidState, BlockPos waterfallPos, FluidState waterfallState) {
        if (!Util.isChunkLoaded(level, waterfallPos.getX(), waterfallPos.getZ())) {
            return EMPTY_WATERFALL_DATA;
        }

        Fluid fluid = waterfallState.getType();
        if (fluid.isSame(fluidState.getType()) && fluidState.isSource()) {
            List<Direction> openSides = new ArrayList<>();

            for (Direction direction : Direction.Plane.HORIZONTAL) {
                if (!Util.isSolidOrNotEmpty(level, waterfallPos.relative(direction))) {
                    openSides.add(direction);
                }
            }

            if (!openSides.isEmpty()) {
                boolean surroundedByWater = false;

                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        if (fluid.isSame(level.getFluidState(waterfallPos.offset(x, 0, z)).getType())) {
                            surroundedByWater = true;
                        }
                    }
                }

                if (surroundedByWater) {
                    return new WaterfallData(1, openSides);
                }
            }
        }

        return EMPTY_WATERFALL_DATA;
    }

    public record WaterfallData(int intensity, List<Direction> openSides) {

        public static final WaterfallData EMPTY_WATERFALL_DATA = new WaterfallData(0, new ArrayList<>());
    }
}
