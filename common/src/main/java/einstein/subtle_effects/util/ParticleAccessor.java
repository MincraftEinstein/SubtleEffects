package einstein.subtle_effects.util;

public interface ParticleAccessor {

    double getX();

    double getY();

    double getZ();

    float getAlpha();

    void setGravity(float gravity);

    boolean subtleEffects$wasForced();

    void subtleEffects$force();
}
