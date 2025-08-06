package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.init.ModParticleRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.Mth;

import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;

public class PotionCloudParticle extends FlatPlaneParticle {

    private final LifetimeAlpha lifetimeAlpha = new LifetimeAlpha(0.5F, 0, 0.5F, 1);

    protected PotionCloudParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, ColorParticleOption option) {
        super(level, x, y, z);
        xd = nextNonAbsDouble(random, 0, 0.03);
        zd = nextNonAbsDouble(random, 0, 0.03);
        lifetime = Mth.nextInt(random, 25, 30);
        rotation.rotateY(90 * random.nextInt(3) * Mth.DEG_TO_RAD).rotateX(-90 * Mth.DEG_TO_RAD);
        alpha = lifetimeAlpha.startAlpha();
        quadSize = 1;
        setSize(2, 0.1F);
        setColor(option.getRed(), option.getGreen(), option.getBlue());
        pickSprite(sprites);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        super.render(consumer, camera, partialTicks);
        alpha = lifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ModParticleRenderTypes.getBlendedOrTransparent();
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<ColorParticleOption> {

        @Override
        public TextureSheetParticle createParticle(ColorParticleOption option, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PotionCloudParticle(level, x, y, z, sprites, option);
        }
    }
}
