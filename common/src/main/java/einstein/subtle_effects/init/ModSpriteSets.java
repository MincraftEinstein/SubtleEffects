package einstein.subtle_effects.init;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModSpriteSets {

    public static final Map<ResourceLocation, ParticleResources.MutableSpriteSet> REGISTERED = new HashMap<>();

    public static final ParticleEngine.MutableSpriteSet WATER_SPLASH_OVERLAY = register("water_splash_overlay");

    public static void init() {
    }

    private static ParticleResources.MutableSpriteSet register(String name) {
        ParticleResources.MutableSpriteSet spriteSet = new ParticleResources.MutableSpriteSet();
        REGISTERED.put(SubtleEffects.loc(name), spriteSet);
        return spriteSet;
    }
}
