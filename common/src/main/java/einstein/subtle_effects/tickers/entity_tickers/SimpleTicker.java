package einstein.subtle_effects.tickers.entity_tickers;

import einstein.subtle_effects.util.EntityProvider;
import net.minecraft.world.entity.Entity;

public class SimpleTicker<T extends Entity> extends EntityTicker<T> {

    private final EntityProvider<T> provider;

    public SimpleTicker(T entity, EntityProvider<T> provider) {
        super(entity);
        this.provider = provider;
    }

    @Override
    public void entityTick() {
        provider.apply(entity, level, random);
    }
}
