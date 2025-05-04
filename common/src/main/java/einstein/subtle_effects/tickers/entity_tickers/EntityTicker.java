package einstein.subtle_effects.tickers.entity_tickers;

import einstein.subtle_effects.tickers.Ticker;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public abstract class EntityTicker<T extends Entity> extends Ticker {

    protected final T entity;
    protected final Level level;
    protected final RandomSource random = RandomSource.create();

    public EntityTicker(T entity) {
        this.entity = entity;
        level = entity.level();
    }

    @Override
    public final void tick() {
        if (!entity.isAlive() || !EntityTickerManager.isEntityInRange(entity, EntityTickerManager.OUTER_RANGE)) {
            remove();
            return;
        }

        if (EntityTickerManager.isEntityInRange(entity, EntityTickerManager.INNER_RANGE)) {
            entityTick();
        }
    }

    protected abstract void entityTick();
}
