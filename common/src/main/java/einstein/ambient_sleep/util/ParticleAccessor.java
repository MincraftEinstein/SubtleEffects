package einstein.ambient_sleep.util;

public interface ParticleAccessor {

    double getX();

    double getY();

    double getZ();

    boolean ambientSleep$wasForced();

    void ambientSleep$force();
}
