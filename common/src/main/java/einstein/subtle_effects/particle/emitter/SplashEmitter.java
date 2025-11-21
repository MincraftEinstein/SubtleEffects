package einstein.subtle_effects.particle.emitter;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import einstein.subtle_effects.particle.option.SplashDropletParticleOptions;
import einstein.subtle_effects.particle.option.SplashEmitterParticleOptions;
import einstein.subtle_effects.particle.option.SplashParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;
import static einstein.subtle_effects.util.MathUtil.*;

public class SplashEmitter extends NoRenderParticle {

    @Nullable
    private final Entity entity;
    private final float velocity;
    private final float absVelocity;
    private final boolean isLava;
    private final TagKey<Fluid> fluidTag;
    private final float widthModifier;
    private final float heightModifier;
    private final float xScale;
    private final float yScale;
    private final ParticleType<SplashParticleOptions> splashParticle;
    private final ParticleType<FloatParticleOptions> splashRippleParticle;
    private final ParticleType<SplashDropletParticleOptions> dropletParticle;
    private final BlockPos.MutableBlockPos pos;
    private boolean firstSplash = true;
    private boolean secondSplash = true;

    protected SplashEmitter(ClientLevel level, double x, double y, double z, TagKey<Fluid> fluidTag, ParticleType<SplashParticleOptions> splashParticle, ParticleType<FloatParticleOptions> splashRippleParticle, ParticleType<SplashDropletParticleOptions> dropletParticle, boolean isLava, SplashEmitterParticleOptions options) {
        super(level, x, y, z);
        this.fluidTag = fluidTag;
        this.splashRippleParticle = splashRippleParticle;
        this.isLava = isLava;
        this.splashParticle = splashParticle;
        this.dropletParticle = dropletParticle;
        lifetime = 8; // half the splash lifetime
        pos = BlockPos.containing(x, y, z).mutable();
        velocity = options.velocity();
        absVelocity = Mth.abs(velocity);
        widthModifier = options.widthModifier();
        heightModifier = options.heightModifier();
        xScale = widthModifier + 0.5F;
        yScale = absVelocity * heightModifier * widthModifier * 2;

        if (options.entityId() > -1) {
            entity = level.getEntity(options.entityId());
            return;
        }
        entity = null;
    }

    public static SplashEmitterParticleOptions createForEntity(Entity entity, ParticleType<SplashEmitterParticleOptions> splashParticle, double yVelocity) {
        float entityWidth = entity.getBbWidth();
        float entityHeight = entity.getBbHeight();
        Entity passenger = entity.getFirstPassenger();

        if (passenger != null) {
            entityWidth = Math.max(entityWidth, passenger.getBbWidth());
            entityHeight += (passenger.getBbHeight() / 2);
        }

        return new SplashEmitterParticleOptions(splashParticle, entityWidth, entityHeight, (float) yVelocity, entity.getId());
    }

    @Override
    public void tick() {
        if (removed) {
            return;
        }

        super.tick();
        pos.set(x, y, z);

        boolean isInFluid = level.getFluidState(pos).is(fluidTag) || Util.getCauldronFluid(level.getBlockState(pos)).is(fluidTag);
        if (!isInFluid || onGround) {
            remove();
            return;
        }

        if (firstSplash) {
            spawnSplashParticles(xScale, yScale, ((yScale * 0.5F) / widthModifier) * 0.3F, xScale);
            firstSplash = false;

            if (ENTITIES.splashes.splashBubbles && entity != null) {
                if (level.getFluidState(pos.below()).is(FluidTags.WATER)) {
                    for (int i = 0; i < 8 * (widthModifier * 5); i++) {
                        int xSign = nextSign(random);
                        int zSign = nextSign(random);

                        level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP,
                                x + nextDouble(random, 0.1) * xSign,
                                random.nextInt(3) == 0 ? Mth.nextDouble(random, entity.getY() + heightModifier, y) : entity.getRandomY(),
                                z + nextDouble(random, 0.1) * zSign,
                                nextNonAbsDouble(random, xScale),
                                -absVelocity * 2 * Mth.nextDouble(random, 0.5, 2),
                                nextNonAbsDouble(random, xScale)
                        );
                    }
                }
            }

            if (!ENTITIES.splashes.secondarySplash || velocity > -ENTITIES.splashes.secondarySplashVelocityThreshold.get()) {
                remove();
            }
            return;
        }

        if (age >= lifetime && secondSplash) {
            spawnSplashParticles(xScale / 2, yScale * 1.5F, ((yScale * 0.85F) / widthModifier) * 0.3F, xScale * 0.8F);
            secondSplash = false;
        }
    }

    private void spawnSplashParticles(float xScale, float yScale, float dropletYSpeed, float dropletXSpeed) {
        level.addAlwaysVisibleParticle(new SplashParticleOptions(splashParticle, xScale, yScale),
                true, x, y, z, 0, 0, 0
        );

        if (hasRipple()) {
            level.addAlwaysVisibleParticle(new FloatParticleOptions(splashRippleParticle, xScale),
                    true, x, y, z, 0, 0, 0
            );
        }

        if (!ENTITIES.splashes.splashDroplets) {
            return;
        }

        SplashDropletParticleOptions dropletOptions = new SplashDropletParticleOptions(dropletParticle, Math.min(this.xScale, 2), isLava ? 1 : ENTITIES.splashes.splashOverlayTint.get());
        for (int i = 0; i < 4 * widthModifier; i++) {
            level.addParticle(dropletOptions,
                    x + nextNonAbsDouble(random, xScale),
                    y + (nextDouble(random, 0.6) * yScale),
                    z + nextNonAbsDouble(random, xScale),
                    0, dropletYSpeed, 0
            );
        }

        for (int i = 0; i < 8 * widthModifier; i++) {
            int xSign = nextSign(random);
            int zSign = nextSign(random);

            level.addParticle(dropletOptions,
                    x + nextDouble(random, xScale) * xSign,
                    y + (nextDouble(random, 0.6) * yScale),
                    z + nextDouble(random, xScale) * zSign,
                    Mth.nextDouble(random, 0.01, 0.1) * dropletXSpeed * xSign,
                    dropletYSpeed,
                    Mth.nextDouble(random, 0.01, 0.1) * dropletXSpeed * zSign
            );
        }
    }

    private boolean hasRipple() {
        if (ENTITIES.splashes.splashRipples) {
            if (!ENTITIES.splashes.lavaSplashRipples && isLava) {
                return false;
            }
            return firstSplash || (secondSplash && ENTITIES.splashes.secondarySplashRipples);
        }
        return false;
    }

    public record Provider() implements ParticleProvider<SplashEmitterParticleOptions> {

        @Override
        public Particle createParticle(SplashEmitterParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SplashEmitter(level, x, y, z, FluidTags.WATER, ModParticles.WATER_SPLASH.get(), ModParticles.WATER_SPLASH_RIPPLE.get(), ModParticles.WATER_SPLASH_DROPLET.get(), false, options);
        }
    }

    public record LavaProvider() implements ParticleProvider<SplashEmitterParticleOptions> {

        @Override
        public Particle createParticle(SplashEmitterParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SplashEmitter(level, x, y, z, FluidTags.LAVA, ModParticles.LAVA_SPLASH.get(), ModParticles.LAVA_SPLASH_RIPPLE.get(), ModParticles.LAVA_SPLASH_DROPLET.get(), true, options);
        }
    }
}
