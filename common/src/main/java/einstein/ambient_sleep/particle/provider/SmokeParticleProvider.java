package einstein.ambient_sleep.particle.provider;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class SmokeParticleProvider extends SmokeParticle.Provider {

    public SmokeParticleProvider(SpriteSet sprites) {
        super(sprites);
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Particle particle = super.createParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed);
        particle.setColor(1, 1, 1);
        return particle;
    }
}
