package einstein.subtle_effects.particle.provider;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CherryParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public record AzaleaParticleProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        // Cherry particles don't have a provider
        CherryParticle particle = new CherryParticle(level, x, y, z, sprites) {};
        particle.scale(2);
        return particle;
    }
}
