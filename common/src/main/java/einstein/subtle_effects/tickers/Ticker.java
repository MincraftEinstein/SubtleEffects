package einstein.subtle_effects.tickers;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public abstract class Ticker<T extends Entity> {

    protected final T entity;
    protected final Level level;
    protected final RandomSource random = RandomSource.create();
    private boolean isRemoved;

    public Ticker(T entity) {
        this.entity = entity;
        level = entity.level();
    }

    public abstract void tick();

    public void remove() {
        isRemoved = true;
    }

    public boolean isRemoved() {
        return isRemoved;
    }
}
