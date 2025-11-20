package einstein.subtle_effects.particle;

import einstein.subtle_effects.data.FluidPair;
import einstein.subtle_effects.data.FluidPairReloadListener;
import einstein.subtle_effects.data.splash_types.SplashType;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.option.DropletParticleOptions;
import einstein.subtle_effects.particle.option.SplashDropletParticleOptions;
import einstein.subtle_effects.util.DripParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.joml.Vector3f;

public class DropletParticle extends DripParticle.FallAndLandParticle implements DripParticleAccessor {

    private final int lightLevel;

    protected DropletParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float scale, SpriteSet sprites, int lightLevel, Fluid fluid, SimpleParticleType landParticle, boolean isSilent) {
        super(level, x, y, z, fluid, landParticle);
        this.lightLevel = lightLevel;
        setParticleSpeed(xSpeed, ySpeed, zSpeed);
        pickSprite(sprites);
        scale(scale * 1.5F);
        gravity = 0.06F;

        if (isSilent) {
            subtleEffects$setSilent();
        }
    }

    @Override
    public int getLightColor(float partialTick) {
        return Math.max(LightTexture.block(lightLevel), super.getLightColor(partialTick));
    }

    @Override
    public void tick() {
        super.tick();

        if (y == yo) {
            remove();
        }
    }

    @Override
    protected void postMoveUpdate() {
        // noinspection ConstantConditions
        if (landParticle != null) {
            super.postMoveUpdate();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<DropletParticleOptions> {

        @Override
        public Particle createParticle(DropletParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TextureSheetParticle particle = new DropletParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.scale(), sprites, 0, Fluids.WATER, ParticleTypes.SPLASH, options.isSilent());
            int waterColor = level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor();
            float colorIntensity = options.colorIntensity();
            float whiteIntensity = 1 - colorIntensity;
            float red = (waterColor >> 16 & 255) / 255F;
            float green = (waterColor >> 8 & 255) / 255F;
            float blue = (waterColor & 255) / 255F;

            particle.setColor(
                    whiteIntensity + (colorIntensity * red),
                    whiteIntensity + (colorIntensity * green),
                    whiteIntensity + (colorIntensity * blue)
            );
            return particle;
        }
    }

    public record LavaProvider(SpriteSet sprites) implements ParticleProvider<DropletParticleOptions> {

        @Override
        public Particle createParticle(DropletParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DropletParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.scale(), sprites, 15, Fluids.LAVA, ParticleTypes.LANDING_LAVA, options.isSilent());
        }
    }

    public record SplashProvider(SpriteSet sprites) implements ParticleProvider<SplashDropletParticleOptions> {

        @Override
        public Particle createParticle(SplashDropletParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FluidPair fluidPair = FluidPairReloadListener.FLUID_PAIRS.get(options.fluidPairId());
            SplashType type = fluidPair.splashType().orElseThrow();
            Vector3f color = SplashParticle.getColorAndApplyTint(type.dropletOptions(), level, BlockPos.containing(x, y, z), level.getRandom());
            Particle particle = new DropletParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.scale(), sprites, type.lightEmission(), fluidPair.source(), fluidPair.is(Fluids.WATER) ? ParticleTypes.SPLASH : (fluidPair.is(Fluids.LAVA) ? ParticleTypes.LANDING_LAVA : null), ModConfigs.ENTITIES.splashes.splashDropletSounds);
            particle.setColor(color.x(), color.y(), color.z());
            return particle;
        }
    }
}
