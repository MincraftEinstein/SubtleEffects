package einstein.subtle_effects.particle.provider;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class SplashDropletParticle extends DripParticle.FallAndLandParticle {

    private final boolean glowing;

    protected SplashDropletParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float scale, SpriteSet sprites, boolean glowing, Fluid fluid, SimpleParticleType landParticle) {
        super(level, x, y, z, fluid, landParticle);
        this.glowing = glowing;
        setParticleSpeed(xSpeed, ySpeed, zSpeed);
        pickSprite(sprites);
        scale(scale * 1.5F);
        gravity = 0.06F;
    }

    @Override
    public int getLightColor(float partialTick) {
        if (glowing) {
            return 240;
        }
        return super.getLightColor(partialTick);
    }

    @Override
    public void tick() {
        super.tick();

        if (y == yo) {
            remove();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<FloatParticleOptions> {

        @Override
        public Particle createParticle(FloatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TextureSheetParticle particle = new SplashDropletParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.f(), sprites, false, Fluids.WATER, ParticleTypes.SPLASH);
            int waterColor = level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor();
            float colorIntensity = ModConfigs.ENTITIES.splashes.splashOverlayTint.get();
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

    public record LavaProvider(SpriteSet sprites) implements ParticleProvider<FloatParticleOptions> {

        @Override
        public Particle createParticle(FloatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SplashDropletParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.f(), sprites, true, Fluids.LAVA, ParticleTypes.LANDING_LAVA);
        }
    }
}
