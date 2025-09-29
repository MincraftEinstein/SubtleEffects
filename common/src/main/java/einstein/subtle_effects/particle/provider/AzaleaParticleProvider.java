package einstein.subtle_effects.particle.provider;

import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CherryParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public record AzaleaParticleProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        // Cherry particles don't have a provider
        CherryParticle particle = new CherryParticle(level, x, y, z, sprites) {};
        particle.scale(2);

        // noinspection all
        ParticleAccessor accessor = (ParticleAccessor) particle;
        accessor.setSizes(accessor.getWidth() / 4, accessor.getHeight());
        return particle;
    }
}
