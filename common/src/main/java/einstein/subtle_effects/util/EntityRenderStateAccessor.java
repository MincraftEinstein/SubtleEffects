package einstein.subtle_effects.util;

public interface EntityRenderStateAccessor {

    <T> T subtleEffects$get(Key<T> key);

    <T> void subtleEffects$set(Key<T> key, T value);

    record Key<T>(String key) {

    }
}
