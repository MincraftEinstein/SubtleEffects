package einstein.subtle_effects.util;

public interface ParticleAccessor {

    double getX();

    double getY();

    double getZ();

    float getAlpha();

    boolean subtleEffects$wasForced();

    void subtleEffects$force();
}
