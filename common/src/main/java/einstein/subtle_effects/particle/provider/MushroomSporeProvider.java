package einstein.subtle_effects.particle.provider;

import einstein.subtle_effects.util.MathUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public record MushroomSporeProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
        xSpeed = MathUtil.nextNonAbsDouble(random, 0.2);
        zSpeed = MathUtil.nextNonAbsDouble(random, 0.2);
        SuspendedParticle particle = new SuspendedParticle(level, x, y, z, xSpeed, 0, zSpeed, sprites.get(random));
        float color = random.nextFloat() * 0.1F + 0.2F;
        particle.setColor(color, color, color);
        return particle;
    }
}
