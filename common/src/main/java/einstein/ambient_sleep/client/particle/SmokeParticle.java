package einstein.ambient_sleep.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class SmokeParticle extends BaseAshSmokeParticle {

    protected SmokeParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, 0.1F, 0.1F, 0.1F, xSpeed, ySpeed, zSpeed, 1, sprites, 0, 8, -0.1F, true);
        setColor(1, 1, 1);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SmokeParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
