package einstein.subtle_effects.particle;

import com.google.common.base.Suppliers;
import einstein.subtle_effects.data.DropletOptions;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.data.FluidDefinitionReloadListener;
import einstein.subtle_effects.data.splash_types.SplashType;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.option.DropletParticleOptions;
import einstein.subtle_effects.util.DripParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluid;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.function.Supplier;

public class DropletParticle extends DripParticle.FallAndLandParticle implements DripParticleAccessor {

    public static final Supplier<DropletParticleOptions> WATER = Suppliers.memoize(() -> new DropletParticleOptions(FluidDefinitionReloadListener.WATER_ID, false, 1, true));
    private final int lightLevel;
    private final FluidDefinition fluidDefinition;
    private final boolean fromSplash;

    protected DropletParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float scale, TextureAtlasSprite sprite, int lightLevel, Fluid fluid, SimpleParticleType landParticle, boolean isSilent, FluidDefinition fluidDefinition, boolean fromSplash) {
        super(level, x, y, z, fluid, landParticle, sprite);
        this.lightLevel = lightLevel;
        this.fluidDefinition = fluidDefinition;
        this.fromSplash = fromSplash;
        setParticleSpeed(xSpeed, ySpeed, zSpeed);
        scale(scale * 1.5F);
        gravity = 0.06F;

        if (isSilent) {
            subtleEffects$setSilent();
        }
    }

    @Override
    public int getLightCoords(float partialTick) {
        return Math.max(LightCoordsUtil.block(lightLevel), super.getLightCoords(partialTick));
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
    public Layer getLayer() {
        return Layer.OPAQUE;
    }

    public FluidDefinition getFluidDefinition() {
        return fluidDefinition;
    }

    public boolean isFromSplash() {
        return fromSplash;
    }

    public record SplashProvider(SpriteSet sprites) implements ParticleProvider<DropletParticleOptions> {

        @Override
        public Particle createParticle(DropletParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            boolean fromSplash = options.fromSplash();
            FluidDefinition fluidDefinition = FluidDefinitionReloadListener.DEFINITIONS.get(options.fluidDefinitionId());
            Optional<SplashType> splashType = fluidDefinition.splashType();
            DropletOptions fluidDropletOptions = fluidDefinition.dropletOptions();
            DropletOptions dropletOptions = fromSplash && splashType.isPresent() ? splashType.get().dropletOptions().orElse(fluidDropletOptions) : fluidDropletOptions;
            Vector3f color = dropletOptions.getColorAndApplyTint(level, BlockPos.containing(x, y, z), level.getRandom());

            // noinspection ConstantConditions
            DropletParticle particle = new DropletParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.scale(), sprites.get(random), fluidDefinition.lightEmission(), fluidDefinition.source(),
                    dropletOptions.landParticle().orElse(null), fromSplash ? ModConfigs.ENTITIES.splashes.splashDropletSounds.get() : options.isSilent(),
                    fluidDefinition, fromSplash);
            particle.setColor(color.x(), color.y(), color.z());
            return particle;
        }
    }
}
