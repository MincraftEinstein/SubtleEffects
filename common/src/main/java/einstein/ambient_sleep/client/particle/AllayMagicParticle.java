package einstein.ambient_sleep.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class AllayMagicParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    public AllayMagicParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        friction = 0.96F;
        gravity = -0.1F;
        xd = (xd * 0.1) + xSpeed;
        yd = (yd * 0.1) + ySpeed;
        zd = (zd * 0.1) + zSpeed;
        quadSize *= 0.75F * 0.8F;
        lifetime = random.nextIntBetweenInclusive(20, 35);
        hasPhysics = false;
        speedUpWhenYMotionIsBlocked = true;
        float multiplier = random.nextFloat() * 0.4F + 0.6F;
        setColor(randomizeColor(0.133F, multiplier), randomizeColor(0.812F, multiplier), randomizeColor(1, multiplier));
        setSpriteFromAge(sprites);
    }

    protected float randomizeColor(float color, float multiplier) {
        return (random.nextFloat() * 0.2F + 0.8F) * color * multiplier;
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
    protected int getLightColor(float partialTick) {
        return 240 | super.getLightColor(partialTick) >> 16 & 0xFF << 16;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new AllayMagicParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
