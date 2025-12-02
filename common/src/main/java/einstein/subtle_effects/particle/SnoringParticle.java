package einstein.subtle_effects.particle;

import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SnoringParticle extends SmokeParticle {

    protected SnoringParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, 1, sprites);
        setColor(1, 1, 1);
        setLifetime(20);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new SnoringParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }

    public record FallingProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            SnoringParticle particle = new SnoringParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
            ((ParticleAccessor) particle).setGravity(0.1F);
            return particle;
        }
    }
}
