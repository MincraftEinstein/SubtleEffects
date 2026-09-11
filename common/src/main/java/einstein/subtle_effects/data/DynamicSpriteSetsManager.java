package einstein.subtle_effects.data;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicSpriteSetsManager {

    public static final Map<ResourceLocation, SpriteSetHolder> STATIC_SPRITE_SETS = new HashMap<>();
    private static final Map<ResourceLocation, SpriteSetHolder> REGISTERED_SPRITE_SETS = new HashMap<>();
    private static final Map<ResourceLocation, SpriteSetHolder> SPRITE_SETS = new HashMap<>();
    private static final List<ResourceLocation> REMOVED_SPRITE_SETS = new ArrayList<>();

    public static SpriteSetHolder getOrCreate(ResourceLocation id) {
        if (REGISTERED_SPRITE_SETS.containsKey(id)) {
            return REGISTERED_SPRITE_SETS.get(id);
        }
        else if (STATIC_SPRITE_SETS.containsKey(id)) {
            return STATIC_SPRITE_SETS.get(id);
        }
        else if (SPRITE_SETS.containsKey(id)) {
            return SPRITE_SETS.get(id);
        }

        SpriteSetHolder holder = new SpriteSetHolder();
        REGISTERED_SPRITE_SETS.put(id, holder);
        return holder;
    }

    public static void beginReload(Map<ResourceLocation, ParticleEngine.MutableSpriteSet> spriteSets) {
        SubtleEffects.LOGGER.info("Began loading dynamic sprite sets");
        Map<ResourceLocation, SpriteSetHolder> preparedHolders = new HashMap<>(STATIC_SPRITE_SETS);
        REGISTERED_SPRITE_SETS.forEach((id, holder) -> {
            if (preparedHolders.containsKey(id)) {
                SubtleEffects.LOGGER.error("Found duplicate sprite set holder with id '{}'", id);
                return;
            }

            preparedHolders.put(id, holder);
        });
        REGISTERED_SPRITE_SETS.clear();

        SPRITE_SETS.forEach((id, holder) -> {
            if (!preparedHolders.containsKey(id) && !holder.referencesPreExisting()) {
                REMOVED_SPRITE_SETS.add(id);
            }
        });
        SPRITE_SETS.clear();
        SPRITE_SETS.putAll(preparedHolders);
        SPRITE_SETS.forEach((id, holder) -> {
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
        REMOVED_SPRITE_SETS.forEach(spriteSets::remove);
        REMOVED_SPRITE_SETS.clear();
        SubtleEffects.LOGGER.info("Finished loading dynamic sprite sets");
    }
}
