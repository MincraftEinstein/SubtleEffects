package einstein.subtle_effects.tickers.geyser_tickers;

import einstein.subtle_effects.particle.option.GeyserSpoutParticleOptions;
import einstein.subtle_effects.tickers.Ticker;
import einstein.subtle_effects.tickers.TickerManager;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static einstein.subtle_effects.init.ModConfigs.ENVIRONMENT;
import static einstein.subtle_effects.tickers.geyser_tickers.GeyserManager.ACTIVE_GEYSERS;
import static einstein.subtle_effects.tickers.geyser_tickers.GeyserManager.INACTIVE_GEYSERS;

public abstract class GeyserTicker extends Ticker {

    protected final GeyserType type;
    protected final Level level;
    protected final BlockPos pos;
    protected final RandomSource random;
    protected final int lifeTime;
    protected int age;

    public GeyserTicker(GeyserType type, Level level, BlockPos pos, RandomSource random) {
        this.type = type;
        this.level = level;
        this.pos = pos;
        this.random = random;
        lifeTime = getTickDelay(type.activeTime.get());
        updateGeyserPositions(ACTIVE_GEYSERS, list -> list.add(pos));
    }

    protected abstract ParticleOptions getParticle();

    protected final void updateGeyserPositions(Map<GeyserType, List<BlockPos>> map, Consumer<List<BlockPos>> consumer) {
        if (!map.containsKey(type)) {
            map.put(type, new ArrayList<>());
        }
        consumer.accept(map.get(type));
    }

    public static void trySpawn(GeyserType type, Level level, BlockPos pos, RandomSource random) {
        if ((!ACTIVE_GEYSERS.containsKey(type) || !ACTIVE_GEYSERS.get(type).contains(pos)) &&
                (!INACTIVE_GEYSERS.containsKey(type) || !INACTIVE_GEYSERS.get(type).contains(pos))) {
            if (checkLocation(type, level, pos, true)) {
                TickerManager.add(type.geyserTickerProvider.apply(level, pos, random));
            }
        }
    }

    public static boolean checkLocation(GeyserType type, Level level, BlockPos pos, boolean checkAbove) {
        if (type.spawnableBlocks.contains(level.getBlockState(pos).getBlock())) {
            BlockPos abovePos = pos.above();
            if (isNotFaceSturdyOrFluidEmpty(level, abovePos)) {
                if (checkAbove) {
                    for (int i = 1; i < 5; i++) {
                        if (!isNotFaceSturdyOrFluidEmpty(level, abovePos.relative(Direction.UP, i))) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isNotFaceSturdyOrFluidEmpty(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return !state.isFaceSturdy(level, pos, Direction.DOWN) && !(state.getBlock() instanceof BaseFireBlock) && level.getFluidState(pos).isEmpty();
    }

    @Override
    public void tick() {
        age++;

        if (age >= lifeTime) {
            remove();
            return;
        }

        if (Util.isChunkLoaded(level, pos.getX(), pos.getZ())) {
            if (checkLocation(type, level, pos, false)) {
                if (age == 1) {
                    level.addParticle(new GeyserSpoutParticleOptions(type, lifeTime),
                            pos.getX() + 0.5,
                            pos.getY() + 1.001,
                            pos.getZ() + 0.5,
                            0, 0, 0
                    );
                }

                geyserTick();
                return;
            }
        }
        remove();
    }

    protected abstract void geyserTick();

    @Override
    public void remove() {
        super.remove();
        updateGeyserPositions(ACTIVE_GEYSERS, list -> list.remove(pos));
        updateGeyserPositions(INACTIVE_GEYSERS, list -> list.add(pos));
        TickerManager.schedule(getTickDelay(type.inactiveTime.get()),
                () -> updateGeyserPositions(INACTIVE_GEYSERS, list -> list.remove(pos))
        );
    }

    private int getTickDelay(int max) {
        return max >= 300 ? Mth.nextInt(random, max - 200, max) : max;
    }
}
