package einstein.subtle_effects.particle;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.option.FallenLeafParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Objects;

import static einstein.subtle_effects.util.MathUtil.*;

public class FallenLeafParticle extends FlatPlaneParticle {

    private final LifetimeAlpha lifetimeAlpha;

    protected FallenLeafParticle(ClientLevel level, double x, double y, double z, double xSpeed, double zSpeed, FallenLeafParticleOptions options) {
        super(level, x, y, z, Objects.requireNonNull(options.sprite()));
        if (!options.onGround() && ModConfigs.GENERAL.leavesLandingOnWaterKeepMomentum.get()) {
            xd = nextDouble(random, (xSpeed / 4) * 3);
            zd = nextDouble(random, (zSpeed / 4) * 3);
        }

        int baseLifetime = ModConfigs.GENERAL.fallenLeavesLifeTime.get();
        lifetime = baseLifetime + (random.nextInt(10) * nextSign(random));
        lifetimeAlpha = new LifetimeAlpha(options.alpha(), 0, 0.5F, 1);
        alpha = lifetimeAlpha.startAlpha();
        rotation = rotation.rotateX(toRadians(90));
        friction = 0.96F;

        // noinspection all
        sprite = options.sprite();
        roll = options.rotation();
        oRoll = roll;
        quadSize = options.quadSize();
        setSize(options.bbWidth(), options.bbHeight());
        Vector3f color = options.color();
        setColor(color.x, color.y, color.z);
    }

    @Override
    public void extract(QuadParticleRenderState state, Camera renderInfo, float partialTicks) {
        alpha = lifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks);
        super.extract(state, renderInfo, partialTicks);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider() implements ParticleProvider<FallenLeafParticleOptions> {

        @Override
        public @Nullable Particle createParticle(FallenLeafParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return options == FallenLeafParticleOptions.EMPTY ? null : new FallenLeafParticle(level, x, y, z, xSpeed, zSpeed, options);
        }
    }
}
