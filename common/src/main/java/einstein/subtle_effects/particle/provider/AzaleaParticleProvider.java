package einstein.subtle_effects.particle.provider;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class AzaleaParticleProvider extends FallingLeavesParticle.CherryProvider {

    public AzaleaParticleProvider(SpriteSet sprites) {
        super(sprites);
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Particle particle = super.createParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed);
        particle.scale(2);
        return particle;
    }
}
