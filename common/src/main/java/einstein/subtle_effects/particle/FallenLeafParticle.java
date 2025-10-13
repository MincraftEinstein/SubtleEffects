package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.option.FallenLeafParticleOptions;
import einstein.subtle_effects.util.MathUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class FallenLeafParticle extends FlatPlaneParticle {

    private final LifetimeAlpha lifetimeAlpha;

    protected FallenLeafParticle(ClientLevel level, double x, double y, double z, double xSpeed, double zSpeed, FallenLeafParticleOptions options) {
        super(level, x, y, z);
        if (!options.onGround() && ModConfigs.GENERAL.leavesLandingOnWaterKeepMomentum) {
            xd = MathUtil.nextDouble(random, (xSpeed / 4) * 3);
            zd = MathUtil.nextDouble(random, (zSpeed / 4) * 3);
        }

        lifetime = Mth.nextInt(random, 30, 50);
        lifetimeAlpha = new LifetimeAlpha(options.alpha(), 0, 0.5F, 1);
        alpha = lifetimeAlpha.startAlpha();
        rotation = rotation.rotateX(90 * Mth.DEG_TO_RAD);
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
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        super.render(buffer, renderInfo, partialTicks);
        alpha = lifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks);
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
