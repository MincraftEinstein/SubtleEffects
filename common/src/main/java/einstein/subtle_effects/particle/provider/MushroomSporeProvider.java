package einstein.subtle_effects.particle.provider;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public record MushroomSporeProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

    @Nullable
    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        RandomSource random = level.getRandom();
        xSpeed = random.nextGaussian() * 1.0E-6F;
        ySpeed = random.nextGaussian() * 1.0E-4F;
        zSpeed = random.nextGaussian() * 1.0E-6F;
        SuspendedParticle particle = new SuspendedParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
        float color = random.nextFloat() * 0.1F + 0.2F;
        particle.setColor(color, color, color);
        return particle;
    }
}
