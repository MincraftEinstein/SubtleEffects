package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.FloatParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class SplashRippleParticle extends FlatPlaneParticle {

    private final BlockPos.MutableBlockPos pos;
    private final SpriteSet sprites;
    private final TagKey<Fluid> fluidTag;
    private final boolean glowing;
    private boolean shouldAnimate = false;

    protected SplashRippleParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, float xScale, TagKey<Fluid> fluidTag, boolean glowing) {
        super(level, x, y, z);
        this.sprites = sprites;
        this.fluidTag = fluidTag;
        this.glowing = glowing;
        pos = BlockPos.containing(x, y, z).mutable();
        rotation = rotation.rotateX(90 * Mth.DEG_TO_RAD);
        lifetime = 15;
        xScale /= 2; // Divided by 2 because it is used as the distance from the center
        xScale -= (xScale * 0.0625F * 4F); // Subtracts 4 in scale so it aligns with the splash particle
        quadSize = xScale;
        setSize(xScale, 0.1F);
        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        pos.set(x, y, z);
        if (!level.getFluidState(pos).is(fluidTag) && !Util.getCauldronFluid(level.getBlockState(pos)).is(fluidTag)) {
            remove();
            return;
        }

        if (age++ >= lifetime) {
            if (shouldAnimate) {
                remove();
                return;
            }

            shouldAnimate = true;
            lifetime = 20;
            age = 0;
        }

        quadSize += 0.005F;

        if (shouldAnimate) {
            setSpriteFromAge(sprites);
        }
    }

    @Override
    protected int getLightColor(float partialTick) {
        if (glowing) {
            return 240;
        }
        return super.getLightColor(partialTick);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<FloatParticleOptions> {

        @Override
        public Particle createParticle(FloatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SplashRippleParticle particle = new SplashRippleParticle(level, x, y, z, sprites, options.f(), FluidTags.WATER, false);
            int waterColor = level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor();
            float colorIntensity = ENTITIES.splashes.splashOverlayTint.get();

            if (colorIntensity > 0) {
                float whiteIntensity = 1 - colorIntensity;
                particle.setColor(whiteIntensity + (colorIntensity * ((waterColor >> 16 & 255) / 255F)),
                        whiteIntensity + (colorIntensity * ((waterColor >> 8 & 255) / 255F)),
                        whiteIntensity + (colorIntensity * ((waterColor & 255) / 255F)));
            }

            particle.setAlpha(ENTITIES.splashes.splashOverlayAlpha.get());
            return particle;
        }
    }

    public record LavaProvider(SpriteSet sprites) implements ParticleProvider<FloatParticleOptions> {

        @Override
        public Particle createParticle(FloatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SplashRippleParticle particle = new SplashRippleParticle(level, x, y, z, sprites, options.f(), FluidTags.LAVA, true);
            if (ENTITIES.splashes.splashOverlayAlpha.get() == 0) {
                particle.setAlpha(0);
            }

            return particle;
        }
    }
}
