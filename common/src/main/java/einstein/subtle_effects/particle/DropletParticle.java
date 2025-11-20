package einstein.subtle_effects.particle;

import com.google.common.base.Suppliers;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.data.DropletOptions;
import einstein.subtle_effects.data.FluidPair;
import einstein.subtle_effects.data.FluidPairReloadListener;
import einstein.subtle_effects.data.splash_types.SplashType;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.option.DropletParticleOptions;
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

import java.util.Optional;
import java.util.function.Supplier;

public class DropletParticle extends DripParticle.FallAndLandParticle implements DripParticleAccessor {

    public static final Supplier<DropletParticleOptions> WATER = Suppliers.memoize(() -> new DropletParticleOptions(SubtleEffects.loc("water"), false, 1, true));
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

    public record SplashProvider(SpriteSet sprites) implements ParticleProvider<DropletParticleOptions> {

        @Override
        public Particle createParticle(DropletParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            boolean splash = options.isSplash();
            FluidPair fluidPair = FluidPairReloadListener.FLUID_PAIRS.get(options.fluidPairId());
            Optional<SplashType> splashType = fluidPair.splashType();
            DropletOptions dropletOptions = splash && splashType.isPresent() ? splashType.get().dropletOptions() : fluidPair.dropletOptions();
            Vector3f color = dropletOptions.getColorAndApplyTint(level, BlockPos.containing(x, y, z), level.getRandom());
            Particle particle = new DropletParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.scale(), sprites, fluidPair.lightEmission(), fluidPair.source(),
                    fluidPair.is(Fluids.WATER) ? ParticleTypes.SPLASH : (fluidPair.is(Fluids.LAVA) ? ParticleTypes.LANDING_LAVA : null),
                    splash ? ModConfigs.ENTITIES.splashes.splashDropletSounds : options.isSilent()
            );
            particle.setColor(color.x(), color.y(), color.z());
            return particle;
        }
    }
}
