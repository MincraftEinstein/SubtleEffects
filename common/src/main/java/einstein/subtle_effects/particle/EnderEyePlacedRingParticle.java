package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class EnderEyePlacedRingParticle extends FlatPlaneParticle {

    public static final float SIZE = 0.2501F;
    private final LifetimeAlpha lifetimeAlpha = new LifetimeAlpha(1, 0, 0, 1);

    protected EnderEyePlacedRingParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        lifetime = 10;
        quadSize = SIZE;
        setSize(SIZE, SIZE);
        alpha = lifetimeAlpha.startAlpha();
    }

    @Override
    protected void renderQuad(VertexConsumer consumer, Camera camera, float partialTicks) {
        alpha = lifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks);
        Vec3 cameraPos = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, xo, this.x) - cameraPos.x());
        float y = (float) (Mth.lerp(partialTicks, yo, this.y) - cameraPos.y());
        float z = (float) (Mth.lerp(partialTicks, zo, this.z) - cameraPos.z());

        renderQuad(consumer, new Quaternionf(), partialTicks, x, y, z + SIZE);
        renderQuad(consumer, new Quaternionf().rotateY(90 * Mth.DEG_TO_RAD), partialTicks, x - SIZE, y, z);
        renderQuad(consumer, new Quaternionf().rotateY(180 * Mth.DEG_TO_RAD), partialTicks, x, y, z - SIZE);
        renderQuad(consumer, new Quaternionf().rotateY(-90 * Mth.DEG_TO_RAD), partialTicks, x + SIZE, y, z);
    }

    protected void renderQuad(VertexConsumer consumer, Quaternionf rotation, float partialTicks, float x, float y, float z) {
        renderQuad(consumer, rotation, partialTicks, x, y, z, false);
        renderQuad(consumer, rotation, partialTicks, x, y, z, true);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<ColorParticleOption> {

        @Override
        public Particle createParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            EnderEyePlacedRingParticle particle = new EnderEyePlacedRingParticle(level, x, y, z);
            particle.pickSprite(sprites);
            particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
            return particle;
        }
    }
}
