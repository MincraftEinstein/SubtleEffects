package einstein.subtle_effects.ticking.tickers;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public abstract class LevelTicker extends Ticker {

    protected final Level level;
    protected final RandomSource random;

    public LevelTicker(Level level, RandomSource random) {
        this.level = level;
        this.random = random;
    }

    public LevelTicker(Level level) {
        this(level, RandomSource.create());
    }
}
