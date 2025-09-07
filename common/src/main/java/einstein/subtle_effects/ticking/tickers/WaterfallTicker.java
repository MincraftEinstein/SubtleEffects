package einstein.subtle_effects.ticking.tickers;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.MathUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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

    public static void trySpawn(Level level, BlockPos pos) {
        if (!WATERFALLS.containsKey(pos)) {
            BlockPos waterfallPos = pos.above();
            FluidState waterfallState = level.getFluidState(waterfallPos);
            WaterfallData data = getWaterfallIntensity(level, pos, waterfallPos, waterfallState);

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

            WaterfallType type = data.type();
            if (type == WaterfallType.NORMAL) {
                level.addParticle(ModParticles.CASCADE.get(),
                        waterfallPos.getX() + xOffset + MathUtil.nextDouble(random, 0.2),
                        waterfallPos.getY() + 0.2F,
                        waterfallPos.getZ() + zOffset + MathUtil.nextDouble(random, 0.2),
                        nextDouble(random, 0.5, 1) * flow.x(),
                        0,
                        nextDouble(random, 0.5, 1) * flow.z()
                );
            }
            else if (type == WaterfallType.SMALL) {
                level.addParticle(ModParticles.LARGE_CASCADE_DROPLET.get(),
                        waterfallPos.getX() + xOffset,
                        waterfallPos.getY() + 0.2F,
                        waterfallPos.getZ() + zOffset,
                        nextDouble(random, 0.1, 0.3) * flow.x(),
                        0,
                        nextDouble(random, 0.1, 0.3) * flow.z()
                );
            }
            else if (type == WaterfallType.LARGE) {
                for (int i = 0; i < 6; i++) {
                    level.addParticle(ModParticles.CASCADE.get(),
                            waterfallPos.getX() + xOffset + (MathUtil.nextDouble(random, 0.3) * direction.getStepX()),
                            waterfallPos.getY() + MathUtil.nextDouble(random, 2) + 0.2F,
                            waterfallPos.getZ() + zOffset + (MathUtil.nextDouble(random, 0.2) * direction.getStepZ()),
                            nextDouble(random, 0.5, 1) * flow.x(),
                            0,
                            nextDouble(random, 0.5, 1) * flow.z()
                    );
                }

                if (random.nextInt(3) == 0) {
                    level.addParticle(ModParticles.WATERFALL_MIST.get(),
                            waterfallPos.getX() + xOffset,
                            waterfallPos.getY() + 1,
                            waterfallPos.getZ() + zOffset,
                            nextDouble(random, 0.5, 1) * (flow.x() * 0.5),
                            nextDouble(random, 0.3, 0.7),
                            nextDouble(random, 0.5, 1) * (flow.z() * 0.5)
                    );
                }
            }
        }

        if (updateTicks++ >= MAX_UPDATE_TICKS) {
            WaterfallData newData = getWaterfallIntensity(level, pos, waterfallPos, fluidState);
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

    private static WaterfallData getWaterfallIntensity(Level level, BlockPos lakePos, BlockPos waterfallPos, FluidState waterfallState) {
        if (!Util.isChunkLoaded(level, waterfallPos.getX(), waterfallPos.getZ())) {
            return EMPTY_WATERFALL_DATA;
        }

        Fluid fluid = waterfallState.getType();
        FluidState lakeFluidState = level.getFluidState(lakePos);
        if (fluid.isSame(lakeFluidState.getType()) && lakeFluidState.isSource() && !level.getBlockState(waterfallPos).is(Blocks.BUBBLE_COLUMN)) {
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
                        if (x == 0 && z == 0) {
                            continue;
                        }

                        if (fluid.isSame(level.getFluidState(lakePos.offset(x, 0, z)).getType())) {
                            surroundedByWater = true;
                        }
                    }
                }

                if (surroundedByWater) {
                    int distance = 0;

                    for (int i = 19; i > -1; i--) { // 'i' offset by 1 so it doesn't count the bottom waterfall block
                        BlockPos pos1 = waterfallPos.above(20 - i);
                        FluidState fluidState1 = level.getFluidState(pos1);

                        if (fluid.isSame(fluidState1.getType()) && !fluidState1.isSource()) {
                            distance++;
                        }
                    }

                    boolean canBeLarge = false;
                    WaterfallType type = WaterfallType.NORMAL;

                    if (distance >= 0 && distance <= 5) {
                        type = WaterfallType.SMALL;
                    }
                    else if (distance > 10) {
                        int size = 0;
                        canBeLarge = true;

                        for (Direction direction : Direction.Plane.HORIZONTAL) {
                            BlockPos relativePos = lakePos.relative(direction);
                            WaterfallTicker relativeTicker = WATERFALLS.get(relativePos);

                            if (relativeTicker != null && relativeTicker.data.canBeLarge()) {
                                size++;

                                BlockPos nextPos = relativePos.relative(direction);
                                WaterfallTicker nextTicker = WATERFALLS.get(nextPos);
                                if (nextTicker != null && nextTicker.data.canBeLarge()) {
                                    size++;
                                    break;
                                }
                            }
                        }

                        if (size >= 2) {
                            type = WaterfallType.LARGE;
                        }
                    }

                    return new WaterfallData(type, canBeLarge, openSides);
                }
            }
        }

        return EMPTY_WATERFALL_DATA;
    }

    public record WaterfallData(WaterfallType type, boolean canBeLarge, List<Direction> openSides) {

        public static final WaterfallData EMPTY_WATERFALL_DATA = new WaterfallData(WaterfallType.NORMAL, false, new ArrayList<>());
    }

    public enum WaterfallType {
        NORMAL,
        SMALL,
        LARGE
    }
}
