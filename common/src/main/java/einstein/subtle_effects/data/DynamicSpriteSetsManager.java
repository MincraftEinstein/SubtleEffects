package einstein.subtle_effects.data;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicSpriteSetsManager {

    private static final Map<Identifier, SpriteSetHolder> SPRITE_SETS = new HashMap<>();
    private static final Map<Identifier, SpriteSetHolder> REGISTERED_SPRITE_SETS = new HashMap<>();
    public static final Map<Identifier, SpriteSetHolder> STATIC_SPRITE_SETS = new HashMap<>();

    public static SpriteSetHolder getOrCreate(Identifier id) {
        if (REGISTERED_SPRITE_SETS.containsKey(id)) {
            return REGISTERED_SPRITE_SETS.get(id);
        }
        else if (STATIC_SPRITE_SETS.containsKey(id)) {
            return STATIC_SPRITE_SETS.get(id);
        }

        SpriteSetHolder holder = new SpriteSetHolder(id);
        REGISTERED_SPRITE_SETS.put(id, holder);
        return holder;
    }

    public static void reload(Map<Identifier, ParticleResources.MutableSpriteSet> spriteSets) {
        List<Identifier> oldSpriteSets = new ArrayList<>(SPRITE_SETS.keySet());
        oldSpriteSets.forEach(location -> {
            SpriteSetHolder holder = SPRITE_SETS.get(location);
            if (!holder.referencesPreExisting()) {
                spriteSets.remove(location);
            }
        });

        Map<Identifier, SpriteSetHolder> preparedHolders = new HashMap<>(STATIC_SPRITE_SETS);
        REGISTERED_SPRITE_SETS.forEach((id, holder) -> {
            if (preparedHolders.containsKey(id)) {
                SubtleEffects.LOGGER.error("Found duplicate sprite set holder with id '{}'", id);
                return;
            }

            preparedHolders.put(id, holder);
        });

        SPRITE_SETS.clear();
        SPRITE_SETS.putAll(preparedHolders);
        SPRITE_SETS.forEach((id, holder) -> {
            if (!spriteSets.containsKey(id)) {
                ParticleResources.MutableSpriteSet spriteSet = new ParticleResources.MutableSpriteSet();
                holder.set(spriteSet, false);
                spriteSets.put(id, spriteSet);
                return;
            }

            holder.set(spriteSets.get(id), true);
        });

        REGISTERED_SPRITE_SETS.clear();
    }
}
