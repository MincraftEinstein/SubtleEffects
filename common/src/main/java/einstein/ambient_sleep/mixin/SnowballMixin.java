package einstein.ambient_sleep.mixin;

import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.util.ParticleTicker;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Snowball.class)
public class SnowballMixin implements ParticleTicker {

    @Override
    public void ambientSleep$particleTick(Level level, RandomSource random, double x, double y, double z) {
        level.addParticle(ModParticles.SNOWBALL_TRAIL.get(), x + random.nextDouble(), y + random.nextDouble(), z + random.nextDouble(), 0, 0, 0);
    }
}
