package einstein.ambient_sleep.mixin;

import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.util.ParticleEmittingEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Chicken.class)
public class ChickenMixin implements ParticleEmittingEntity {

    @Override
    public void ambientSleep$spawnParticles(Level level, Entity entity, RandomSource random) {
        for (int i = 0; i < 10; i++) {
            level.addParticle(ModParticles.CHICKEN_FEATHER.get(), entity.getX(), entity.getY(), entity.getZ(), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1));
        }
    }
}
