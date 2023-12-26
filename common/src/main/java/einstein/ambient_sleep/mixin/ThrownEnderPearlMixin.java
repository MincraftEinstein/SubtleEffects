package einstein.ambient_sleep.mixin;

import einstein.ambient_sleep.util.ParticleTicker;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrownEnderpearl.class)
public class ThrownEnderPearlMixin implements ParticleTicker {

    @Override
    public void ambientSleep$particleTick(Level level, RandomSource random, double x, double y, double z) {
        for (int i = 0; i < 10; i++) {
            level.addParticle(ParticleTypes.PORTAL, x + random.nextDouble(), y + random.nextDouble(), z + random.nextDouble(), 0, 0, 0);
        }
    }
}
