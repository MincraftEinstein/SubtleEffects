package einstein.subtle_effects.particle.provider;

import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ColorParticleOption;

public class PotionPoofCloudProvider extends SpellParticle.MobEffectProvider {

    public PotionPoofCloudProvider(SpriteSet sprites) {
        super(sprites);
    }

    @Override
    public Particle createParticle(ColorParticleOption option, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Particle particle = super.createParticle(option, level, x, y, z, xSpeed, ySpeed, zSpeed);
        particle.scale(3.5F);
        ((ParticleAccessor) particle).setHasPhysics(true);
        return particle;
    }
}
