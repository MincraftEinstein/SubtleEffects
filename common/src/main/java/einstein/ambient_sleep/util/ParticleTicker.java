package einstein.ambient_sleep.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public interface ParticleTicker {

    void ambientSleep$particleTick(Level level, RandomSource random, double x, double y, double z);
}
