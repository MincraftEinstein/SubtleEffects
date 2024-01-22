package einstein.ambient_sleep.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface ParticleEmittingEntity {

    void ambientSleep$spawnParticles(Level level, Entity entity, RandomSource random);
}
