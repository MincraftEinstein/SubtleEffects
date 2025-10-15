package einstein.subtle_effects.util;

import einstein.subtle_effects.ticking.tickers.entity.EntityTicker;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public interface EntityMixinAccessor {

    Int2ObjectMap<EntityTicker<?>> subtleEffects$getTickers();

    boolean subtleEffects$wasTouchingLava();

    void subtleEffects$setTouchingLava(boolean touchingLava);
}
