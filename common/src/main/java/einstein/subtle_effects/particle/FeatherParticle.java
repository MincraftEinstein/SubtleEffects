package einstein.subtle_effects.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class FeatherParticle extends TextureSheetParticle {

    private float rollSpeed = 0.1F;

    protected FeatherParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        lifetime = 30;
        setSize(0.1F, 0.1F);
        pickSprite(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FeatherParticle particle = new FeatherParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
            particle.quadSize = 0.1F;
            particle.gravity = 0.35F;
            return particle;
        }
    }

    public record BoneProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FeatherParticle particle = new FeatherParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
            particle.gravity = 0.7F;
            particle.rollSpeed = 0.05F;
            return particle;
        }
    }
}
