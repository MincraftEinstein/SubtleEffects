package einstein.subtle_effects.ticking.tickers;

import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public abstract class BlockPosTicker extends LevelTicker {

    public static final int INT = 16 * 3; // 16 blocks per chunk * 3 chunks
    protected final BlockPos pos;
    protected final Minecraft minecraft = Minecraft.getInstance();

    public BlockPosTicker(Level level, BlockPos pos) {
        super(level);
        this.pos = pos;
    }

    public BlockPosTicker(Level level, RandomSource random, BlockPos pos) {
        super(level, random);
        this.pos = pos;
    }

    @Override
    public final void tick() {
        if (Util.isChunkLoaded(level, pos.getX(), pos.getZ())) {
            if (!shouldCheckDistance() || (minecraft.player != null && pos.distToCenterSqr(minecraft.player.position()) < INT * INT)) {
                positionedTick();
            }
        }
    }

    protected abstract void positionedTick();

    protected boolean shouldCheckDistance() {
        return true;
    }
}
