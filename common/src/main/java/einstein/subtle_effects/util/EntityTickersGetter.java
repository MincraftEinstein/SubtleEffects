package einstein.subtle_effects.util;

import einstein.subtle_effects.ticking.tickers.entity.EntityTicker;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public interface EntityTickersGetter {

    Int2ObjectMap<EntityTicker<?>> subtleEffects$getTickers();
}
