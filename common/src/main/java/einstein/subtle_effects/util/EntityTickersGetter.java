package einstein.subtle_effects.util;

import einstein.subtle_effects.tickers.entity_tickers.EntityTicker;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.List;

public interface EntityTickersGetter {

    Int2ObjectMap<EntityTicker<?>> subtleEffects$getTickers();
}
