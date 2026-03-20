package einstein.subtle_effects.particle;

import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.data.FluidDefinitionReloadListener;
import einstein.subtle_effects.data.splash_types.SplashOptions;
import einstein.subtle_effects.data.splash_types.SplashType;
import einstein.subtle_effects.particle.option.RippleParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

public class SplashRippleParticle extends FlatPlaneParticle {

    private final BlockPos.MutableBlockPos pos;
    private final SpriteSet sprites;
    private final FluidDefinition fluidDefinition;
    private final int lightLevel;
    private boolean shouldAnimate = false;

    protected SplashRippleParticle(ClientLevel level, double x, double y, double z, RippleParticleOptions options) {
        super(level, x, y, z, null);
        fluidDefinition = FluidDefinitionReloadListener.DEFINITIONS.get(options.fluidDefinitionId());
        SplashType type = fluidDefinition.splashType().orElseThrow();
        SplashOptions rippleOptions = type.splashRippleOptions();
        sprites = rippleOptions.holder().get();
        lightLevel = fluidDefinition.lightEmission();
        pos = BlockPos.containing(x, y, z).mutable();
        rotation = rotation.rotateX(90 * Mth.DEG_TO_RAD);
        lifetime = 15;
        alpha = SplashParticle.alpha(rippleOptions);
        Vector3f color = rippleOptions.getColorAndApplyTint(level, pos, random);
        rCol = color.x;
        gCol = color.y;
        bCol = color.z;

        float scale = options.scale();
        scale /= 2; // Divided by 2 because it is used as the distance from the center
        scale -= (scale * 0.0625F * 4F); // Subtracts 4 in scale so it aligns with the splash particle
        quadSize = scale;
        setSize(scale, 0.1F);
        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        pos.set(x, y, z);
        if (SplashParticle.canNotSurvive(fluidDefinition, level, pos)) {
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
    protected int getLightCoords(float partialTick) {
        return Math.max(LightCoordsUtil.block(lightLevel), super.getLightCoords(partialTick));
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider() implements ParticleProvider<RippleParticleOptions> {

        @Override
        public Particle createParticle(RippleParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new SplashRippleParticle(level, x, y, z, options);
        }
    }
}
