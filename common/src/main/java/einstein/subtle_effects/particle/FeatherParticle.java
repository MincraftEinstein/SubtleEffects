package einstein.subtle_effects.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.SimpleParticleType;

public class FeatherParticle extends TextureSheetParticle {

    protected FeatherParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        quadSize = 0.1F;
        gravity = 0.35F;
        lifetime = 30;
        setSize(0.1F, 0.1F);
        pickSprite(sprites);
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
            roll = oRoll + 0.1F * -(age / 10F);
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FeatherParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
