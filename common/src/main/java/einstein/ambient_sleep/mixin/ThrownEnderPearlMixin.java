package einstein.ambient_sleep.mixin;

import einstein.ambient_sleep.util.EntityParticleTicker;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrownEnderpearl.class)
public class ThrownEnderPearlMixin implements EntityParticleTicker {

    @Override
    public void ambientSleep$particleTick(Level level, Entity entity, RandomSource random) {
        for (int i = 0; i < 10; i++) {
            level.addParticle(ParticleTypes.PORTAL, entity.getRandomX(2), entity.getRandomY(), entity.getRandomZ(2), 0, 0, 0);
        }
    }
}
