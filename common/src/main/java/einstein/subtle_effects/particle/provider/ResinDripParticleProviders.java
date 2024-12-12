package einstein.subtle_effects.particle.provider;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

public class ResinDripParticleProviders {

    public record DrippingResinDropProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DripParticle.DripHangParticle particle = new DripParticle.DripHangParticle(level, x, y, z, Fluids.EMPTY, ModParticles.FALLING_RESIN.get());
            setProperties(particle, sprites, ((ParticleAccessor) particle).getGravity() * 0.01F, 100);
            return particle;
        }
    }

    public record FallingResinDropProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DripParticle particle = new DripParticle.FallAndLandParticle(level, x, y, z, Fluids.EMPTY, ModParticles.LANDING_RESIN.get());
            setProperties(particle, sprites, 0.01F, null);
            return particle;
        }
    }

    public record LandingResinDropProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DripParticle particle = new DripParticle.DripLandParticle(level, x, y, z, Fluids.EMPTY);
            setProperties(particle, sprites, null, (int) (28 / (Math.random() * 0.8 + 0.2)));
            return particle;
        }
    }

    private static void setProperties(DripParticle particle, SpriteSet sprites, @Nullable Float gravity, @Nullable Integer lifeTime) {
        if (gravity != null) {
            ((ParticleAccessor) particle).setGravity(gravity);
        }

        if (lifeTime != null) {
            particle.setLifetime(lifeTime);
        }

        particle.pickSprite(sprites);
        particle.setColor(0.925F, 0.447F, 0.078F);
    }
}
