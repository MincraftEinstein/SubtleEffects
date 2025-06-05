package einstein.subtle_effects.particle;

import einstein.subtle_effects.util.MathUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class SculkDustParticle extends GlowingSuspendedParticle {

    public SculkDustParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z) {
        super(level, sprites, x, y, z, 0, 0, 0);
        xd = MathUtil.nextNonAbsDouble(random, 0.01);
        yd = MathUtil.nextNonAbsDouble(random, 0.01);
        zd = MathUtil.nextNonAbsDouble(random, 0.01);
        lifetime *= 2;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SculkDustParticle(level, sprites, x, y, z);
        }
    }
}