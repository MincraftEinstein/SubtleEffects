package einstein.subtle_effects.particle.provider;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.LargeSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class GeyserSmokeParticleProvider extends LargeSmokeParticle.Provider {

    public GeyserSmokeParticleProvider(SpriteSet sprites) {
        super(sprites);
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
        LargeSmokeParticle particle = (LargeSmokeParticle) super.createParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed, random);
        particle.setColor(1, 1, 1);
        particle.setLifetime((int) Math.max(1, 8 / (level.random.nextFloat() * 0.8 + 0.2)));
        return particle;
    }
}
