package einstein.subtle_effects.data;

import einstein.subtle_effects.data.splash_types.SplashTypeReloadListener;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static einstein.subtle_effects.SubtleEffects.LOGGER;

public class DynamicSpriteSetsManager {

    public static final Map<ResourceLocation, SpriteSetHolder> STATIC_SPRITE_SETS = new HashMap<>();
    private static final Map<ResourceLocation, SpriteSetHolder> ADD_QUEUE = new HashMap<>();
    private static final Map<ResourceLocation, SpriteSetHolder> ACTIVE_SPRITE_SETS = new HashMap<>();
    private static final List<ResourceLocation> REMOVE_QUEUE = new ArrayList<>();

    public static SpriteSetHolder getOrCreate(ResourceLocation id) {
        if (ADD_QUEUE.containsKey(id)) {
            return ADD_QUEUE.get(id);
        }
        else if (STATIC_SPRITE_SETS.containsKey(id)) {
            return STATIC_SPRITE_SETS.get(id);
        }

        SpriteSetHolder holder = new SpriteSetHolder();
        ADD_QUEUE.put(id, holder);
        return holder;
    }

    public static void beginReload(Map<ResourceLocation, ParticleEngine.MutableSpriteSet> spriteSets) {
        LOGGER.info("Began loading dynamic sprite sets");
        if (!SplashTypeReloadListener.HAS_PREPARED) {
            LOGGER.warn("Splash Types not prepared in time for Dynamic Sprite Sets");
        }

        Map<ResourceLocation, SpriteSetHolder> preparedHolders = new HashMap<>(STATIC_SPRITE_SETS);
        ADD_QUEUE.forEach((id, holder) -> {
            if (preparedHolders.containsKey(id)) {
                LOGGER.error("Found duplicate sprite set holder with id '{}'", id);
                return;
            }

            preparedHolders.put(id, holder);
        });
        ADD_QUEUE.clear();

        ACTIVE_SPRITE_SETS.forEach((id, holder) -> {
            if (!preparedHolders.containsKey(id) && !holder.referencesPreExisting()) {
                REMOVE_QUEUE.add(id);
            }
        });
        ACTIVE_SPRITE_SETS.clear();
        ACTIVE_SPRITE_SETS.putAll(preparedHolders);
        ACTIVE_SPRITE_SETS.forEach((id, holder) -> {
            if (!spriteSets.containsKey(id)) {
                ParticleEngine.MutableSpriteSet spriteSet = new ParticleEngine.MutableSpriteSet();
                holder.set(spriteSet, false);
                spriteSets.put(id, spriteSet);
                return;
            }

            holder.set(spriteSets.get(id), true);
        });
    }

    public static void finishReload(Map<ResourceLocation, ParticleEngine.MutableSpriteSet> spriteSets) {
        REMOVE_QUEUE.forEach(spriteSets::remove);
        REMOVE_QUEUE.clear();
        LOGGER.info("Finished loading dynamic sprite sets");
    }
}
