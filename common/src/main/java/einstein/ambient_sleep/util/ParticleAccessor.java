package einstein.ambient_sleep.util;

public interface ParticleAccessor {

    double getX();

    double getY();

    double getZ();

    float getAlpha();

    boolean ambientSleep$wasForced();

    void ambientSleep$force();
}
