package einstein.subtle_effects.particle.emitter;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import einstein.subtle_effects.particle.option.IntegerParticleOptions;
import einstein.subtle_effects.particle.option.SplashParticleOptions;
import einstein.subtle_effects.util.MathUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import static einstein.subtle_effects.util.MathUtil.*;

public class SplashEmitter extends NoRenderParticle {

    private final float velocity;
    private final float entityWidth;
    private final float entityHeight;
    private final float xScale;
    private final float yScale;
    private final ParticleType<SplashParticleOptions> splashParticle;
    private final ParticleType<FloatParticleOptions> dropletParticle;
    private boolean firstSplash = true;
    private boolean secondSplash = true;

    protected SplashEmitter(ClientLevel level, double x, double y, double z, ParticleType<SplashParticleOptions> splashParticle, ParticleType<FloatParticleOptions> dropletParticle, int entityId) {
        super(level, x, y, z);
        Entity entity = level.getEntity(entityId);
        lifetime = 8;
        this.splashParticle = splashParticle;
        this.dropletParticle = dropletParticle;

        if (entity != null) {
            velocity = Mth.abs((float) entity.getDeltaMovement().y);
            entityWidth = entity.getBbWidth();
            entityHeight = entity.getBbHeight();
            xScale = entityWidth + 0.5F;
            yScale = velocity * entityHeight * entityWidth * 2;
            return;
        }

        velocity = 0;
        entityWidth = 0;
        entityHeight = 0;
        xScale = 0;
        yScale = 0;
        remove();
    }

    @Override
    public void tick() {
        if (removed) {
            return;
        }

        super.tick();

        if (firstSplash) {
            spawnSplashParticles(xScale, yScale, (yScale * 0.75F) / entityWidth, 0/*1 + (entityWidth * entityHeight) * 0.75F*/);
            firstSplash = false;
            return;
        }

        if (age >= 8 && secondSplash) { // half the splash lifetime
            spawnSplashParticles(xScale / 2, yScale * 1.5F, yScale / entityWidth, 0);
            secondSplash = false;
        }
    }

    private void spawnSplashParticles(float xScale, float yScale, float dropletYSpeed, float dropletScale) {
        level.addAlwaysVisibleParticle(new SplashParticleOptions(splashParticle, xScale, yScale, firstSplash),
                true, x, y, z, 0, 0, 0
        );

        dropletYSpeed = (dropletYSpeed / 2);
        FloatParticleOptions options = new FloatParticleOptions(dropletParticle, 1);
        for (int i = 0; i < 8 * entityWidth; i++) { // count needs to scale with splash size
            level.addParticle(options,
                    x + nextNonAbsDouble(random, xScale),
                    y + nextDouble(random, 0.3),
                    z + nextNonAbsDouble(random, xScale),
                    0, dropletYSpeed, 0
            );

            int xSign = nextSign(random);
            int zSign = nextSign(random);

            level.addParticle(options,
                    x + nextDouble(random, xScale) * xSign,
                    y + nextDouble(random, 0.6),
                    z + nextDouble(random, xScale) * zSign,
                    Mth.nextDouble(random, 0.01, 0.1) * xSign,
                    dropletYSpeed,
                    Mth.nextDouble(random, 0.01, 0.1) * zSign
            );
        }
    }

    public record Provider() implements ParticleProvider<IntegerParticleOptions> {

        @Override
        public Particle createParticle(IntegerParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SplashEmitter(level, x, y, z, ModParticles.WATER_SPLASH.get(), ModParticles.WATER_DROPLET.get(), options.integer());
        }
    }

    public record LavaProvider() implements ParticleProvider<IntegerParticleOptions> {

        @Override
        public Particle createParticle(IntegerParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SplashEmitter(level, x, y, z, ModParticles.ENTITY_LAVA_SPLASH.get(), ModParticles.LAVA_DROPLET.get(), options.integer());
        }
    }
}
