package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.SplashDropletParticleOptions;
import einstein.subtle_effects.util.DripParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class SplashDropletParticle extends DripParticle.FallAndLandParticle implements DripParticleAccessor {

    private final boolean glowing;

    protected SplashDropletParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float scale, TextureAtlasSprite sprite, boolean glowing, Fluid fluid, SimpleParticleType landParticle, boolean isSilent) {
        super(level, x, y, z, fluid, landParticle, sprite );
        this.glowing = glowing;
        setParticleSpeed(xSpeed, ySpeed, zSpeed);
        scale(scale * 1.5F);
        gravity = 0.06F;

        if (isSilent) {
            subtleEffects$setSilent();
        }
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
    public Layer getLayer() {
        return Layer.OPAQUE;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SplashDropletParticleOptions> {

        @Override
        public Particle createParticle(SplashDropletParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            SingleQuadParticle particle = new SplashDropletParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.scale(), sprites.get(random), false, Fluids.WATER, ParticleTypes.SPLASH, options.isSilent());
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

    public record LavaProvider(SpriteSet sprites) implements ParticleProvider<SplashDropletParticleOptions> {

        @Override
        public Particle createParticle(SplashDropletParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new SplashDropletParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.scale(), sprites.get(random), true, Fluids.LAVA, ParticleTypes.LANDING_LAVA, options.isSilent());
        }
    }
}
