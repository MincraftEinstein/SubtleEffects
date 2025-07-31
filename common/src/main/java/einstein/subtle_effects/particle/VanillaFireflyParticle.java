package einstein.subtle_effects.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class VanillaFireflyParticle extends TextureSheetParticle {

    private static final float PARTICLE_FADE_OUT_LIGHT_TIME = 0.3F;
    private static final float PARTICLE_FADE_IN_LIGHT_TIME = 0.1F;
    private static final float PARTICLE_FADE_OUT_ALPHA_TIME = 0.5F;
    private static final float PARTICLE_FADE_IN_ALPHA_TIME = 0.3F;
    private static final int PARTICLE_MIN_LIFETIME = 36;
    private static final int PARTICLE_MAX_LIFETIME = 180;

    private VanillaFireflyParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        speedUpWhenYMotionIsBlocked = true;
        friction = 0.96F;
        quadSize *= 0.75F;
        yd *= 0.8;
        xd *= 0.8;
        zd *= 0.8;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public int getLightColor(float partialTick) {
        return (int) (255 * getFadeAmount(getLifetimeProgress(age + partialTick), PARTICLE_FADE_IN_LIGHT_TIME, PARTICLE_FADE_OUT_LIGHT_TIME));
    }

    public void tick() {
        super.tick();

        if (!level.getBlockState(BlockPos.containing(x, y, z)).isAir()) {
            remove();
            return;
        }

        setAlpha(getFadeAmount(getLifetimeProgress(age), PARTICLE_FADE_IN_ALPHA_TIME, PARTICLE_FADE_OUT_ALPHA_TIME));

        if (random.nextDouble() > 0.95 || age == 1) {
            setParticleSpeed(-0.05 + 0.1 * random.nextDouble(), -0.05 + 0.1 * random.nextDouble(), -0.05 + 0.1 * random.nextDouble());
        }
    }

    private float getLifetimeProgress(float age) {
        return Mth.clamp(age / lifetime, 0, 1);
    }

    private static float getFadeAmount(float lifetimeProgress, float fadeIn, float fadeOut) {
        if (lifetimeProgress >= 1 - fadeIn) {
            return (1 - lifetimeProgress) / fadeIn;
        }
        return lifetimeProgress <= fadeOut ? lifetimeProgress / fadeOut : 1;
    }

    public record FireflyProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            VanillaFireflyParticle particle = new VanillaFireflyParticle(level, x, y, z,
                    0.5F - level.random.nextDouble(),
                    level.random.nextBoolean() ? ySpeed : -ySpeed,
                    0.5F - level.random.nextDouble()
            );

            particle.setLifetime(level.random.nextIntBetweenInclusive(PARTICLE_MIN_LIFETIME, PARTICLE_MAX_LIFETIME));
            particle.scale(1.5F);
            particle.pickSprite(sprites);
            particle.setAlpha(0);
            return particle;
        }
    }
}
