package einstein.ambient_sleep.tickers;

import einstein.ambient_sleep.mixin.client.entity.EntityAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public abstract class Ticker<T extends Entity> {

    protected final T entity;
    protected final Level level;
    protected final RandomSource random;
    private boolean isRemoved;

    public Ticker(T entity) {
        this.entity = entity;
        level = entity.level();
        random = ((EntityAccessor) entity).getRandom();
    }

    public abstract void tick();

    public void remove() {
        isRemoved = true;
    }

    public boolean isRemoved() {
        return isRemoved;
    }
}
