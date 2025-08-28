package einstein.subtle_effects.init;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModSpriteSets {

    public static final Map<ResourceLocation, ParticleEngine.MutableSpriteSet> REGISTERED = new HashMap<>();

    public static final ParticleEngine.MutableSpriteSet WATER_SPLASH_OVERLAY = register("water_splash_overlay");
    public static final ParticleEngine.MutableSpriteSet WATER_SPLASH_BOTTOM = register("water_splash_bottom");
    public static final ParticleEngine.MutableSpriteSet LAVA_SPLASH_OVERLAY = register("lava_splash_overlay");
    public static final ParticleEngine.MutableSpriteSet LAVA_SPLASH_BOTTOM = register("lava_splash_bottom");

    public static void init() {
    }

    private static ParticleEngine.MutableSpriteSet register(String name) {
        ParticleEngine.MutableSpriteSet spriteSet = new ParticleEngine.MutableSpriteSet();
        REGISTERED.put(SubtleEffects.loc(name), spriteSet);
        return spriteSet;
    }
}
