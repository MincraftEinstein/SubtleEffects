package einstein.subtle_effects.particle.provider;

import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SuspendedParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public record PollenProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
        SuspendedParticle particle = new SuspendedParticle(level, sprites, x, y, z, xSpeed, -0.8, zSpeed);
        ((ParticleAccessor) particle).setGravity(0.01F);
        return particle;
    }
}
