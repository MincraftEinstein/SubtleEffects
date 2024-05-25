package einstein.subtle_effects.particle;

import einstein.subtle_effects.util.MathUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class SculkDustParticle extends GlowingSuspendedParticle {

    public SculkDustParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z) {
        super(level, sprites, x, y, z, 0, 0, 0);
        xd = Mth.nextDouble(random, 0, 0.01) * MathUtil.nextSign();
        yd = Mth.nextDouble(random, 0, 0.01) * MathUtil.nextSign();
        zd = Mth.nextDouble(random, 0, 0.01) * MathUtil.nextSign();
        lifetime *= 2;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SculkDustParticle(level, sprites, x, y, z);
        }
    }
}