package einstein.subtle_effects.init;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.data.DynamicSpriteSetsManager;
import einstein.subtle_effects.data.SpriteSetHolder;
import net.minecraft.resources.Identifier;

public class ModSpriteSets {

    public static final SpriteSetHolder WATER_SPLASH = register("water_splash");
    public static final SpriteSetHolder WATER_SPLASH_OVERLAY = register("water_splash_overlay");
    public static final SpriteSetHolder WATER_SPLASH_RIPPLE = register("water_splash_ripple");

    public static void init() {
    }

    private static SpriteSetHolder register(String name) {
        Identifier id = SubtleEffects.loc(name);
        SpriteSetHolder holder = new SpriteSetHolder(id);
        DynamicSpriteSetsManager.STATIC_SPRITE_SETS.put(id, holder);
        return holder;
    }
}
