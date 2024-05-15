package einstein.ambient_sleep.tickers;

import einstein.ambient_sleep.util.ParticleManager;
import net.minecraft.world.entity.Entity;

public class SimpleTicker<T extends Entity> extends Ticker<T>{

    private final ParticleManager.EntityProvider<T> provider;

    public SimpleTicker(T entity, ParticleManager.EntityProvider<T> provider) {
        super(entity);
        this.provider = provider;
    }

    @Override
    public void tick() {
        provider.apply(entity, level, random);
    }
}