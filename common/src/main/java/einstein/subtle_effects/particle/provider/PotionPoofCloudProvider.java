package einstein.subtle_effects.particle.provider;

import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.RandomSource;

public class PotionPoofCloudProvider extends SpellParticle.MobEffectProvider {

    public PotionPoofCloudProvider(SpriteSet sprites) {
        super(sprites);
    }

    @Override
    public Particle createParticle(ColorParticleOption option, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
        Particle particle = super.createParticle(option, level, x, y, z, xSpeed, ySpeed, zSpeed, random);
        particle.scale(3.5F);
        ((ParticleAccessor) particle).setHasPhysics(true);
        return particle;
    }
}
