package einstein.subtle_effects.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class HeartPopParticle extends SingleQuadParticle {

    private final SpriteSet sprites;

    public HeartPopParticle(ClientLevel level, double x, double y, double z, double ySpeed, SpriteSet sprites) {
        super(level, x, y, z, sprites.first());
        this.sprites = sprites;
        xd = 0;
        yd = ySpeed;
        zd = 0;
        lifetime = 5;
        speedUpWhenYMotionIsBlocked = true;
        quadSize *= 1.5F;
        hasPhysics = false;
        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return quadSize * Mth.clamp(age + partialTicks / lifetime * 32, 0, 1);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new HeartPopParticle(level, x, y, z, ySpeed, sprites);
        }
    }
}
