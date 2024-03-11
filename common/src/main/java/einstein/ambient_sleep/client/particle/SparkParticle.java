package einstein.ambient_sleep.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class SparkParticle extends TextureSheetParticle {

    protected SparkParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float lifeTimeModifier, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        friction = 0.96F;
        gravity = -0.1F;
        xd = (xd * 0.1) + xSpeed;
        yd = (yd * 0.1) + ySpeed;
        zd = (yd * 0.1) + zSpeed;
        int i = random.nextInt(11);
        quadSize *= 0.75F * i / 10;
        lifetime = (int) (20 / (random.nextFloat() * 0.8 + 0.2) * i / lifeTimeModifier);
        lifetime = Math.max(lifetime, 1);
        hasPhysics = true;
        speedUpWhenYMotionIsBlocked = true;
        setColor(1, 1, 1);
        pickSprite(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return quadSize * Mth.clamp((age + scaleFactor) / lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public int getLightColor(float partialTick) {
        return 240 | super.getLightColor(partialTick) >> 16 & 0xFF << 16;
    }

    public record LongLifeProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 10, sprites);
        }
    }

    public record ShortLifeProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 20, sprites);
        }
    }

    public record FloatingProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkParticle particle = new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 20, sprites);
            particle.gravity = 0;
            return particle;
        }
    }

    public record MetalProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkParticle particle = new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 20, sprites);
            particle.gravity = 1;
            return particle;
        }
    }
}
