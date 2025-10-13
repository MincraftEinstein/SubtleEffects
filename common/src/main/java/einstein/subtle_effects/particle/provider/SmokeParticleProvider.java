package einstein.subtle_effects.particle.provider;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SmokeParticleProvider extends SmokeParticle.Provider {

    public SmokeParticleProvider(SpriteSet sprites) {
        super(sprites);
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
        SmokeParticle particle = (SmokeParticle) super.createParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed, random);
        particle.setColor(1, 1, 1);
        return particle;
    }
}
