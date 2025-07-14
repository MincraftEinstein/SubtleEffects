package einstein.subtle_effects.particle.emitter;

import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;

public class FireFlyEmitter extends AbstractParticleEmitter {

    protected FireFlyEmitter(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z, 1200, 2, 10, 3);
    }

    @Override
    protected void spawnParticle(double x, double y, double z) {
        level.addParticle(ModConfigs.ENVIRONMENT.fireflies.fireFlyType.getParticle().get(),
                x, y, z,
                nextNonAbsDouble(random, 0.001),
                nextNonAbsDouble(random, 0.001),
                nextNonAbsDouble(random, 0.001)
        );
    }

    public record Provider() implements ParticleProvider<SimpleParticleType> {

        @Override
        public @NotNull Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FireFlyEmitter(level, x, y, z);
        }
    }
}
