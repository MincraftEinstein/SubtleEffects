package einstein.subtle_effects.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class FeatherParticle extends SingleQuadParticle {

    private float rollSpeed = 0.1F;

    protected FeatherParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
        lifetime = 30;
        setSize(0.1F, 0.1F);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        oRoll = roll;

        if (!onGround) {
            roll = oRoll + rollSpeed * -(age / 10F);
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            FeatherParticle particle = new FeatherParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.get(random));
            particle.quadSize = 0.1F;
            particle.gravity = 0.35F;
            return particle;
        }
    }

    public record BoneProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            FeatherParticle particle = new FeatherParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.get(random));
            particle.gravity = 0.7F;
            particle.rollSpeed = 0.05F;
            return particle;
        }
    }
}
