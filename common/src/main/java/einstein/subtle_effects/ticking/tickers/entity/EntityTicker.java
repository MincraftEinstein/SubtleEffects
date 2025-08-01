package einstein.subtle_effects.ticking.tickers.entity;

import einstein.subtle_effects.ticking.tickers.Ticker;
import einstein.subtle_effects.util.EntityTickersGetter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public abstract class EntityTicker<T extends Entity> extends Ticker {

    protected final T entity;
    protected final Level level;
    protected final RandomSource random = RandomSource.create();
    private int id;
    private final boolean checkVisibility;

    public EntityTicker(T entity, boolean checkVisibility) {
        this.entity = entity;
        level = entity.level();
        this.checkVisibility = checkVisibility;
    }

    public EntityTicker(T entity) {
        this(entity, false);
    }

    @Override
    public final void tick() {
        if (!entity.isAlive() || !EntityTickerManager.isEntityInRange(entity, EntityTickerManager.OUTER_RANGE)) {
            remove();
            return;
        }

        if (checkVisibility && entity.isInvisible()) {
            return;
        }

        if (EntityTickerManager.isEntityInRange(entity, EntityTickerManager.INNER_RANGE)) {
            entityTick();
        }
    }

    protected abstract void entityTick();

    @Override
    public void remove() {
        super.remove();
        ((EntityTickersGetter) entity).subtleEffects$getTickers().remove(id);
    }

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }
}
