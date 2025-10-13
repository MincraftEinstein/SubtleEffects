package einstein.subtle_effects.particle.provider;

import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class AzaleaParticleProvider extends FallingLeavesParticle.CherryProvider {

    public AzaleaParticleProvider(SpriteSet sprites) {
        super(sprites);
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
        Particle particle = super.createParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed, random);
        particle.scale(2);

        // noinspection all
        ParticleAccessor accessor = (ParticleAccessor) particle;
        accessor.setSizes(accessor.getWidth() / 4, accessor.getHeight());
        return particle;
    }
}
