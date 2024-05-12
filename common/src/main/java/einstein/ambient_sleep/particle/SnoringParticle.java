package einstein.ambient_sleep.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class SnoringParticle extends SmokeParticle {

    protected SnoringParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, 1, sprites);
        setColor(1, 1, 1);
        setLifetime(20);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SnoringParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }

    public record FallingProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SnoringParticle particle = new SnoringParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
            particle.gravity = 0.1F;
            return particle;
        }
    }
}
