package einstein.subtle_effects.particle;

import einstein.subtle_effects.init.ModParticleLayers;
import einstein.subtle_effects.particle.option.ColorProviderParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;

public class PotionCloudParticle extends FlatPlaneParticle {

    private final LifetimeAlpha lifetimeAlpha = new LifetimeAlpha(0.5F, 0, 0.5F, 1);

    protected PotionCloudParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite, ColorProviderParticleOptions options) {
        super(level, x, y, z, sprite);
        xd = nextNonAbsDouble(random, 0, 0.03);
        zd = nextNonAbsDouble(random, 0, 0.03);
        lifetime = Mth.nextInt(random, 25, 30);
        rotation.rotateY(90 * random.nextInt(3) * Mth.DEG_TO_RAD).rotateX(-90 * Mth.DEG_TO_RAD);
        alpha = lifetimeAlpha.startAlpha();
        quadSize = 1;
        setSize(2, 0.1F);

        Vector3f color = options.provider().provideColor(level, x, y, z, random);
        setColor(color.x(), color.y(), color.z());
    }

    @Override
    public void extract(QuadParticleRenderState state, Camera camera, float partialTicks) {
        alpha = lifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks);
        super.extract(state, camera, partialTicks);
    }

    @Override
    protected Layer getLayer() {
        return ModParticleLayers.getBlendedOrTransparent();
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<ColorProviderParticleOptions> {

        @Override
        public SingleQuadParticle createParticle(ColorProviderParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new PotionCloudParticle(level, x, y, z, sprites.get(random), options);
        }
    }
}
