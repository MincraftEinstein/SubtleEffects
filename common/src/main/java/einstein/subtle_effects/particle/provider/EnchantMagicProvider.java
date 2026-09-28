package einstein.subtle_effects.particle.provider;

import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TrialSpawnerDetectionParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class EnchantMagicProvider extends TrialSpawnerDetectionParticle.Provider {

    public EnchantMagicProvider(SpriteSet sprites) {
        super(sprites);
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Particle particle = super.createParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed);
        particle.setLifetime(Mth.nextInt(level.random, 12, 15));
        particle.setParticleSpeed(0, ((ParticleAccessor) particle).getYSpeed() * 0.5F, 0);
        return particle;
    }
}
