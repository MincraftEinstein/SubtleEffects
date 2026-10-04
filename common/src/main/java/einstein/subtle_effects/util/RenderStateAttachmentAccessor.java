package einstein.subtle_effects.util;

import org.jetbrains.annotations.Nullable;

public interface RenderStateAttachmentAccessor {

    @Nullable
    <T> T subtleEffects$get(Key<T> key);

    default <T> T subtleEffects$get(Key<T> key, T defaultValue) {
        T t = subtleEffects$get(key);
        return t == null ? defaultValue : t;
    }

    <T> void subtleEffects$set(Key<T> key, T value);

    record Key<T>(String key) {

    }
}
