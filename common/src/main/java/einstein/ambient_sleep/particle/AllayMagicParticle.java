package einstein.ambient_sleep.particle;

import einstein.ambient_sleep.particle.option.BooleanParticleOptions;
import einstein.ambient_sleep.util.Util;
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
        setRandomizedColor(0.133F, 0.812F, 1);
        setSpriteFromAge(sprites);
        setAlpha(Mth.clamp(level.random.nextFloat(), 0.5F, 1));
    }

    protected void setRandomizedColor(float r, float g, float b) {
        float multiplier = random.nextFloat() * 0.4F + 0.6F;
        setColor(randomizeColor(r, multiplier), randomizeColor(g, multiplier), randomizeColor(b, multiplier));
    }

    protected float randomizeColor(float color, float multiplier) {
        return (random.nextFloat() * 0.2F + 0.8F) * color * multiplier;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return quadSize * Mth.clamp((age + scaleFactor) / lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return Util.getLightColor(super.getLightColor(partialTick));
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

    public record VexProvider(SpriteSet sprites) implements ParticleProvider<BooleanParticleOptions> {

        @Nullable
        @Override
        public Particle createParticle(BooleanParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            AllayMagicParticle particle = new AllayMagicParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
            if (type.bool()) {
                particle.setRandomizedColor(0.859F, 0.478F, 0.588F);
                return particle;
            }
            particle.setRandomizedColor(0.635F, 0.737F, 0.835F);
            return particle;
        }
    }
}
