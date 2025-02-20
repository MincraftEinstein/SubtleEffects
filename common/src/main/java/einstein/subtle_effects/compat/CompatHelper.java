package einstein.subtle_effects.compat;

import com.google.common.base.Suppliers;
import einstein.subtle_effects.platform.Services;

import java.util.function.Supplier;

public class CompatHelper {

    public static final Supplier<Boolean> IS_SERENE_SEANSONS_LOADED = isLoaded("sereneseasons");
    public static final Supplier<Boolean> IS_SOUL_FIRED_LOADED = isLoaded("soul_fire_d");
    public static final Supplier<Boolean> IS_ITEM_BORDERS_LOADED = isLoaded("itemborders");
    public static final Supplier<Boolean> IS_LEGENDARY_TOOLTIPS_LOADED = isLoaded("legendarytooltips");

    private static Supplier<Boolean> isLoaded(String modId) {
        return Suppliers.memoize(() -> Services.PLATFORM.isModLoaded(modId));
    }
}
