package einstein.subtle_effects.ticking.tickers;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.MathUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static einstein.subtle_effects.ticking.tickers.WaterfallTicker.WaterfallData.EMPTY_WATERFALL_DATA;
import static net.minecraft.util.Mth.nextDouble;

public class WaterfallTicker extends Ticker {

    private static final int MAX_UPDATE_TICKS = 6;
    public static final Map<BlockPos, WaterfallTicker> WATERFALLS = new HashMap<>();
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
        if (fluidState.is(FluidTags.WATER) && fluidState.isSource()) {
            if (!WATERFALLS.containsKey(pos)) {
                BlockPos waterfallPos = pos.above();
                WaterfallData data = evaluateWaterfall(level, pos, waterfallPos);

                if (!data.equals(EMPTY_WATERFALL_DATA)) {
                    WaterfallTicker ticker = new WaterfallTicker(level, pos, waterfallPos, data);

                    WATERFALLS.put(pos, ticker);
                    TickerManager.add(ticker);
                }
            }
        }
    }

    @Override
    public void tick() {
        Vec3 flow = data.flow();

        WaterfallType type = data.type();
        int x = waterfallPos.getX();
        int y = waterfallPos.getY();
        int z = waterfallPos.getZ();

        for (Direction direction : data.openSides()) {
            Direction.Axis axis = direction.getAxis();
            boolean isX = axis == Direction.Axis.X;
            boolean isZ = axis == Direction.Axis.Z;
            double xOffset = isX ? 0.5 + (0.7 * direction.getStepX()) : random.nextFloat();
            double zOffset = isZ ? 0.5 + (0.7 * direction.getStepZ()) : random.nextFloat();
            double xFlow = isX ? flow.x() : 0;
            double zFlow = isZ ? flow.z() : 0;

            if (type == WaterfallType.NORMAL) {
                level.addParticle(ModParticles.WATERFALL_CLOUD.get(),
                        x + xOffset + MathUtil.nextDouble(random, 0.2),
                        y + 0.2F,
                        z + zOffset + MathUtil.nextDouble(random, 0.2),
                        nextDouble(random, 0.5, 1) * xFlow,
                        0,
                        nextDouble(random, 0.5, 1) * zFlow
                );
            }
            else if (type == WaterfallType.SMALL) {
                if (xFlow == 0 && zFlow != 0 || xFlow != 0) {
                    level.addParticle(ModParticles.WATERFALL_DROPLET.get(),
                            x + xOffset - (isX ? (0.2 * direction.getStepX()) : 0),
                            y,
                            z + zOffset - (isZ ? (0.2 * direction.getStepZ()) : 0),
                            nextDouble(random, 0.05, 0.15) * (xFlow == 0 ? 0.01 : (xFlow < 0 ? -1 : 1)),
                            Mth.nextDouble(random, 0.05, 0.1),
                            nextDouble(random, 0.05, 0.15) * (zFlow == 0 ? 0.01 : (zFlow < 0 ? -1 : 1))
                    );
                }
            }
            else if (type == WaterfallType.LARGE) {
                // TODO config for if large waterfalls particles should be force spawned
                for (int i = 0; i < 6; i++) {
                    level.addAlwaysVisibleParticle(ModParticles.WATERFALL_CLOUD.get(), true,
                            x + xOffset + (MathUtil.nextDouble(random, 0.3) * direction.getStepX()),
                            y + MathUtil.nextDouble(random, 2) + 0.2F,
                            z + zOffset + (MathUtil.nextDouble(random, 0.2) * direction.getStepZ()),
                            nextDouble(random, 0.5, 1) * xFlow,
                            0,
                            nextDouble(random, 0.5, 1) * zFlow
                    );
                }

                if (random.nextInt(3) == 0) {
                    level.addAlwaysVisibleParticle(ModParticles.WATERFALL_MIST.get(), true,
                            x + xOffset,
                            y + 1,
                            z + zOffset,
                            nextDouble(random, 0.5, 1) * (xFlow * 0.5),
                            nextDouble(random, 0.3, 0.7),
                            nextDouble(random, 0.5, 1) * (zFlow * 0.5)
                    );
                }
            }
        }

        if (updateTicks++ >= MAX_UPDATE_TICKS) {
            WaterfallData newData = evaluateWaterfall(level, pos, waterfallPos);
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

    private static WaterfallData evaluateWaterfall(Level level, BlockPos lakePos, BlockPos waterfallPos) {
        if (!Util.isChunkLoaded(level, waterfallPos.getX(), waterfallPos.getZ())) {
            return EMPTY_WATERFALL_DATA;
        }

        FluidState waterfallState = level.getFluidState(waterfallPos);
        Fluid waterfallFluid = waterfallState.getType();
        FluidState lakeFluidState = level.getFluidState(lakePos);

        if (waterfallFluid.isSame(lakeFluidState.getType()) && lakeFluidState.isSource() && !level.getBlockState(waterfallPos).is(Blocks.BUBBLE_COLUMN)) {
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

                        if (waterfallFluid.isSame(level.getFluidState(lakePos.offset(x, 0, z)).getType())) {
                            surroundedByWater = true;
                        }
                    }
                }

                if (surroundedByWater) {
                    int distance = 0;

                    for (int i = 19; i > -1; i--) { // 'i' offset by 1 so it doesn't recheck the bottom waterfall block
                        BlockPos abovePos = waterfallPos.above(20 - i);
                        FluidState aboveFluidState = level.getFluidState(abovePos);

                        if (waterfallFluid.isSame(aboveFluidState.getType())) {
                            distance++;
                        }
                    }

                    boolean canBeLarge = false;
                    WaterfallType type = WaterfallType.NORMAL;

                    // the actual water fall height is 2 blocks higher,
                    // 1 for the fluid source and 1 for the bottom block where the ticker is located.
                    // SMALL is 6
                    // LARGE is 10

                    if (distance >= 0 && distance <= 4) {
                        type = WaterfallType.SMALL;
                    }
                    else if (distance > 8) {
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

                    Vec3 flow = waterfallState.getFlow(level, waterfallPos);
                    if (flow.equals(Vec3.ZERO)) {
                        for (Direction openSide : openSides) {
                            flow = flow.add(openSide.getStepX(), 0, openSide.getStepZ());
                        }
                    }

                    return new WaterfallData(type, canBeLarge, openSides, flow);
                }
            }
        }

        return EMPTY_WATERFALL_DATA;
    }

    public record WaterfallData(WaterfallType type, boolean canBeLarge, List<Direction> openSides, Vec3 flow) {

        public static final WaterfallData EMPTY_WATERFALL_DATA = new WaterfallData(WaterfallType.NORMAL, false, new ArrayList<>(), Vec3.ZERO);
    }

    public enum WaterfallType {
        NORMAL,
        SMALL,
        LARGE
    }
}
