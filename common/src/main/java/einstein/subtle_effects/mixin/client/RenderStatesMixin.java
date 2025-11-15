package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.util.RenderStateAttachmentAccessor;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.LevelRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin({EntityRenderState.class, LevelRenderState.class})
public class RenderStatesMixin implements RenderStateAttachmentAccessor {

    @Unique
    private final Map<Key<?>, Object> subtleEffects$stateData = new Reference2ObjectOpenHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T subtleEffects$get(Key<T> key) {
        return (T) subtleEffects$stateData.get(key);
    }

    @Override
    public <T> void subtleEffects$set(Key<T> key, T value) {
        subtleEffects$stateData.put(key, value);
    }
}
