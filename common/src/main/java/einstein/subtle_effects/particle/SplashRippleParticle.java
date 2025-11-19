package einstein.subtle_effects.particle;

import einstein.subtle_effects.data.FluidPair;
import einstein.subtle_effects.data.splash_types.SplashOptionsData;
import einstein.subtle_effects.data.splash_types.SplashTypeData;
import einstein.subtle_effects.data.splash_types.SplashTypeReloadListener;
import einstein.subtle_effects.particle.option.SplashRippleParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class SplashRippleParticle extends FlatPlaneParticle {

    private final BlockPos.MutableBlockPos pos;
    private final SpriteSet sprites;
    private final FluidPair fluidPair;
    private final int lightLevel;
    private boolean shouldAnimate = false;

    protected SplashRippleParticle(ClientLevel level, double x, double y, double z, SplashRippleParticleOptions options) {
        super(level, x, y, z);
        SplashTypeData.SplashType type = SplashTypeReloadListener.SPLASH_TYPES_BY_ID.get(options.type());
        SplashOptionsData.SplashOptions rippleOptions = type.splashRippleOptions();
        sprites = rippleOptions.sprites();
        fluidPair = type.fluidPair();
        lightLevel = type.lightEmission();
        pos = BlockPos.containing(x, y, z).mutable();
        rotation = rotation.rotateX(90 * Mth.DEG_TO_RAD);
        lifetime = 15;
        Vector3f color = SplashParticle.getColorAndApplyTint(rippleOptions, level, pos, random);
        rCol = color.x;
        gCol = color.y;
        bCol = color.z;

        float xScale = options.xScale();
        xScale /= 2; // Divided by 2 because it is used as the distance from the center
        xScale -= (xScale * 0.0625F * 4F); // Subtracts 4 in scale so it aligns with the splash particle
        quadSize = xScale;
        setSize(xScale, 0.1F);
        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        pos.set(x, y, z);
        if (!fluidPair.is(level.getFluidState(pos)) && !fluidPair.is(Util.getCauldronFluid(level.getBlockState(pos)))) {
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
        return Math.max(LightTexture.block(lightLevel), super.getLightColor(partialTick));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider() implements ParticleProvider<SplashRippleParticleOptions> {

        @Override
        public Particle createParticle(SplashRippleParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SplashRippleParticle particle = new SplashRippleParticle(level, x, y, z, options);
//            particle.setAlpha(ENTITIES.splashes.splashOverlayAlpha.get());
            return particle;
        }
    }
}
