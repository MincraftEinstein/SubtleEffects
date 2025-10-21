package einstein.subtle_effects.util;

public interface ParticleAccessor {

    double getX();

    double getY();

    double getZ();

    float getAlpha();

    void setAlpha(float alpha);

    void setGravity(float gravity);

    void setHasPhysics(boolean hasPhysics);

    float getWidth();

    float getHeight();

    void setSizes(float width, float height);

    boolean subtleEffects$wasForced();

    void subtleEffects$force();

    float getGravity();

    boolean subtleEffects$shouldIgnoreCulling();

    void subtleEffects$ignoresCulling();
}
