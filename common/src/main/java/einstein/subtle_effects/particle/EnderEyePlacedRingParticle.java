package einstein.subtle_effects.particle;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticleLayers;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import static einstein.subtle_effects.util.Util.radians;

public class EnderEyePlacedRingParticle extends FlatPlaneParticle {

    public static final int DEFAULT_COLOR = 0x7abaab;
    public static final float SIZE = 0.2501F;
    private final LifetimeAlpha lifetimeAlpha = new LifetimeAlpha(1, 0, 0, 1);

    protected EnderEyePlacedRingParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        lifetime = ModConfigs.BLOCKS.enderEyePlacedRingsDuration.get();
        quadSize = SIZE;
        setSize(SIZE, SIZE);
        alpha = lifetimeAlpha.startAlpha();
    }

    @Override
    protected void renderQuad(QuadParticleRenderState state, Camera camera, float partialTicks, Quaternionf rotation) {
        alpha = lifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks);
        Vec3 cameraPos = camera.position();
        float x = (float) (Mth.lerp(partialTicks, xo, this.x) - cameraPos.x());
        float y = (float) (Mth.lerp(partialTicks, yo, this.y) - cameraPos.y());
        float z = (float) (Mth.lerp(partialTicks, zo, this.z) - cameraPos.z());

        renderQuad(state, new Quaternionf(), partialTicks, x, y, z + SIZE);
        renderQuad(state, new Quaternionf().rotateY(radians(90)), partialTicks, x - SIZE, y, z);
        renderQuad(state, new Quaternionf().rotateY(radians(180)), partialTicks, x, y, z - SIZE);
        renderQuad(state, new Quaternionf().rotateY(radians(-90)), partialTicks, x + SIZE, y, z);
    }

    protected void renderQuad(QuadParticleRenderState state, Quaternionf rotation, float partialTicks, float x, float y, float z) {
        renderQuad(state, rotation, partialTicks, x, y, z, false);
        renderQuad(state, rotation, partialTicks, x, y, z, true);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 240;
    }

    @Override
    protected Layer getLayer() {
        return ModParticleLayers.getBlendedOrTransparent();
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<ColorParticleOption> {

        @Override
        public Particle createParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            EnderEyePlacedRingParticle particle = new EnderEyePlacedRingParticle(level, x, y, z, sprites.get(random));
            particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
            return particle;
        }
    }
}
