package einstein.ambient_sleep.mixin.client;

import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.util.ParticleEmittingEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Parrot.class)
public class ParrotMixin implements ParticleEmittingEntity {

    @Override
    public void ambientSleep$spawnParticles(Level level, Entity entity, RandomSource random) {
        if (entity instanceof Parrot parrot) {
            ParticleOptions particle = switch (parrot.getVariant()) {
                case BLUE -> ModParticles.BLUE_PARROT_FEATHER.get();
                case GRAY -> ModParticles.GRAY_PARROT_FEATHER.get();
                case GREEN -> ModParticles.GREEN_PARROT_FEATHER.get();
                case RED_BLUE -> ModParticles.RED_BLUE_PARROT_FEATHER.get();
                case YELLOW_BLUE -> ModParticles.YELLOW_BLUE_PARROT_FEATHER.get();
            };

            for (int i = 0; i < 5; i++) {
                level.addParticle(particle, entity.getX(), entity.getY(), entity.getZ(), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1));
            }
        }
    }
}
