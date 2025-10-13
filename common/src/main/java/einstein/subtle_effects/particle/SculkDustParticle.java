package einstein.subtle_effects.particle;

import einstein.subtle_effects.util.MathUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SculkDustParticle extends GlowingSuspendedParticle {

    public SculkDustParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, 0, 0, 0, sprite);
        xd = MathUtil.nextNonAbsDouble(random, 0.01);
        yd = MathUtil.nextNonAbsDouble(random, 0.01);
        zd = MathUtil.nextNonAbsDouble(random, 0.01);
        lifetime *= 2;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new SculkDustParticle(level, x, y, z, sprites.get(random));
        }
    }
}