package einstein.ambient_sleep.client.particle.provider;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SuspendedParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public record PollenProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

    @Nullable
    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        RandomSource random = level.getRandom();
        xSpeed = random.nextGaussian() * 1.0E-6F;
        ySpeed = random.nextGaussian() * 1.0E-4F;
        zSpeed = random.nextGaussian() * 1.0E-6F;
        return new SuspendedParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
    }
}
