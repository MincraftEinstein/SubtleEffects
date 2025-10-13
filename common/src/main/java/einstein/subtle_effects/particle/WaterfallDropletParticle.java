package einstein.subtle_effects.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class WaterfallDropletParticle extends BaseWaterfallParticle {

    private final LifetimeAlpha lifetimeAlpha = new LifetimeAlpha(0.7F, 0, 0.5F, 0.8F);

    protected WaterfallDropletParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
        super(level, x, y, z, 0, 0, 0, LifetimeAlpha.ALWAYS_OPAQUE, sprite);
        xd = xSpeed;
        yd = ySpeed;
        zd = zSpeed;
        gravity = 0.3F;
        lifetime = 12;
        alpha = lifetimeAlpha.startAlpha();
        quadSize = Mth.nextFloat(random, 0.0625F, 0.125F);
        setSize(quadSize, quadSize);

        int waterColor = level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor();
        float colorIntensity = Mth.nextFloat(random, 0.2F, 0.5F);
        float whiteIntensity = 1 - colorIntensity;
        float red = (waterColor >> 16 & 255) / 255F;
        float green = (waterColor >> 8 & 255) / 255F;
        float blue = (waterColor & 255) / 255F;

        setColor(
                whiteIntensity + (colorIntensity * red),
                whiteIntensity + (colorIntensity * green),
                whiteIntensity + (colorIntensity * blue)
        );
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

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new WaterfallDropletParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.get(random));
        }
    }
}
