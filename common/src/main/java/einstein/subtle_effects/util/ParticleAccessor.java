package einstein.subtle_effects.util;

public interface ParticleAccessor {

    double getX();

    double getY();

    double getZ();

    float getAlpha();

    void setAlpha(float alpha);

    void setGravity(float gravity);

    void setHasPhysics(boolean hasPhysics);

    boolean subtleEffects$wasForced();

    void subtleEffects$force();

    float getGravity();
}
