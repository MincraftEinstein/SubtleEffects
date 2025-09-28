package einstein.subtle_effects.util;

public interface DripParticleAccessor {

    default void subtleEffects$setSilent() {
    }

    default boolean subtleEffects$isSilent() {
        return false;
    }
}
