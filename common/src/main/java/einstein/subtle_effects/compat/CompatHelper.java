package einstein.subtle_effects.compat;

import com.google.common.base.Suppliers;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.Util;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CompatHelper {

    public static final String PARTICLE_EFFECTS_MOD_ID = "particle-effects";
    public static final String PARTICLE_RAIN_MOD_ID = "particlerain";

    public static final Supplier<Boolean> IS_SERENE_SEANSONS_LOADED = isLoaded("sereneseasons");
    public static final Supplier<Boolean> IS_SOUL_FIRED_LOADED = isLoaded("soul_fire_d");
    public static final Supplier<Boolean> IS_ITEM_BORDERS_LOADED = isLoaded("itemborders");
    public static final Supplier<Boolean> IS_LEGENDARY_TOOLTIPS_LOADED = isLoaded("legendarytooltips");
    public static final Supplier<Boolean> IS_END_REMASTERED_LOADED = isLoaded("endrem");
    public static final Supplier<Boolean> IS_IRIS_LOADED = isLoaded("iris");
    public static final Supplier<Boolean> IS_PARTICLE_EFFECTS_LOADED = isLoaded(PARTICLE_EFFECTS_MOD_ID);
    public static final Supplier<Boolean> IS_PARTICLE_RAIN_LOADED = isLoaded(PARTICLE_RAIN_MOD_ID);

    public static final IntegerProperty FD_PIE_BITES = IntegerProperty.create("bites", 0, 3);
    public static final IntegerProperty JMC_TWO_TIERED_CAKE_BITES = IntegerProperty.create("bites", 0, 10);
    public static final IntegerProperty JMC_THREE_TIERED_CAKE_BITES = IntegerProperty.create("bites", 0, 15);

    public static void init() {
        if (IS_IRIS_LOADED.get()) {
            IrisCompat.init();
        }
    }

    private static Supplier<Boolean> isLoaded(String modId) {
        return Suppliers.memoize(() -> Services.PLATFORM.isModLoaded(modId));
    }

    public static Map<ResourceLocation, ValidatedColor.ColorHolder> getDefaultEyes() {
        Map<ResourceLocation, ValidatedColor.ColorHolder> eyes = new HashMap<>();
        putEye(eyes, "black_eye", 0x020c26);
        putEye(eyes, "cold_eye", 0x40b5c0);
        putEye(eyes, "corrupted_eye", 0x575f5b);
        putEye(eyes, "cursed_eye", 0x3c0d6a);
        putEye(eyes, "guardian_eye", 0xdc8c8c);
        putEye(eyes, "lost_eye", 0xa60828);
        putEye(eyes, "magical_eye", 0x007c86);
        putEye(eyes, "nether_eye", 0x6b482e);
        putEye(eyes, "old_eye", 0xcb9a18);
        putEye(eyes, "rogue_eye", 0x0fea55);
        putEye(eyes, "evil_eye", 0x4a3bc8);
        putEye(eyes, "cryptic_eye", 0xb7e45a);
        putEye(eyes, "wither_eye", 0xc5cfe5);
        putEye(eyes, "witch_eye", 0xc28dcc);
        putEye(eyes, "undead_eye", 0xc0d5c5);
        putEye(eyes, "exotic_eye", 0xb9ecf7);
        return eyes;
    }

    private static void putEye(Map<ResourceLocation, ValidatedColor.ColorHolder> eyes, String name, int color) {
        eyes.put(endRemLoc(name), Util.toColorHolder(color));
    }

    public static ResourceLocation endRemLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("endrem", path);
    }
}
