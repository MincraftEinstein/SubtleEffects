package einstein.subtle_effects.ticking.tickers.entity;

import einstein.subtle_effects.ticking.tickers.LevelTicker;
import einstein.subtle_effects.util.EntityTickerAccessor;
import net.minecraft.world.entity.Entity;

public abstract class EntityTicker<T extends Entity> extends LevelTicker {

    protected final T entity;
    private int id;
    private final boolean checkVisibility;

    public EntityTicker(T entity, boolean checkVisibility) {
        super(entity.level());
        this.entity = entity;
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
        ((EntityTickerAccessor) entity).subtleEffects$getTickers().remove(id);
    }

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }
}
