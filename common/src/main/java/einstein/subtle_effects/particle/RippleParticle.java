package einstein.subtle_effects.particle;

import einstein.subtle_effects.data.DropletOptions;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.data.FluidDefinitionReloadListener;
import einstein.subtle_effects.data.splash_types.SplashType;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.RippleParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Optional;

public class RippleParticle extends FlatPlaneParticle {

    public static final RippleParticleOptions WATER = new RippleParticleOptions(ModParticles.RIPPLE.get(), FluidDefinitionReloadListener.WATER_ID, 1, false);

    private final SpriteSet sprites;

    protected RippleParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, float scale) {
        super(level, x, y, z);
        this.sprites = sprites;
        rotation.rotateX(90 * Mth.DEG_TO_RAD);
        setSpriteFromAge(sprites);
        scale(scale);
        lifetime = 5;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<RippleParticleOptions> {

        @Nullable
        @Override
        public Particle createParticle(RippleParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FluidDefinition fluidDefinition = FluidDefinitionReloadListener.DEFINITIONS.get(options.fluidDefinitionId());

            return getRippleOptions(fluidDefinition, fluidDefinition.splashType(), options).map(rippleOptions -> {
                RippleParticle particle = new RippleParticle(level, x, y, z, sprites, options.scale());
                Vector3f color = rippleOptions.getColorAndApplyTint(level, BlockPos.containing(x, y, z), level.getRandom());

                particle.setColor(color.x(), color.y(), color.z());
                particle.setAlpha(SplashParticle.alpha(rippleOptions.transparency()));
                return particle;
            }).orElse(null);
        }

        private static Optional<DropletOptions.RippleOptions> getRippleOptions(FluidDefinition fluidDefinition, Optional<SplashType> splashType, RippleParticleOptions options) {
            Optional<DropletOptions.RippleOptions> fluidRippleOptions = fluidDefinition.dropletOptions().rippleOptions();
            if (options.fromSplash() && splashType.isPresent()) {
                Optional<DropletOptions> dropletOptions = splashType.get().dropletOptions();

                if (dropletOptions.isPresent()) {
                    Optional<DropletOptions.RippleOptions> rippleOptions = dropletOptions.get().rippleOptions();
                    return rippleOptions.isPresent() ? rippleOptions : fluidRippleOptions;
                }
            }
            return fluidRippleOptions;
        }
    }
}
