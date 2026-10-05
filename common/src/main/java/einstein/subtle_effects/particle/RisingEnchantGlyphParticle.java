package einstein.subtle_effects.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import static einstein.subtle_effects.init.ModConfigs.GENERAL;
import static einstein.subtle_effects.util.Util.PARTICLE_LIGHT_COLOR;

public class RisingEnchantGlyphParticle extends SingleQuadParticle {

    private final LifetimeAlpha lifetimeAlpha = new Particle.LifetimeAlpha(0.5F, 0, 0, 1);

    protected RisingEnchantGlyphParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        xd = 0;
        yd = Mth.nextDouble(random, 0.02, 0.06);
        zd = 0;
        hasPhysics = false;
        lifetime = (int) (random.nextDouble() * 10) + 30;
        quadSize = 0.1F * (random.nextFloat() * 0.5F + 0.2F);

        if (GENERAL.translucentEnchantmentGlyphParticles) {
            alpha = 0.5F;
        }

        if (!GENERAL.disableRandomizedEnchantmentGlyphShading) {
            float f = random.nextFloat() * 0.6F + 0.4F;
            rCol = 0.9F * f;
            gCol = 0.9F * f;
            bCol = f;
        }
    }

    @Override
    public void extract(QuadParticleRenderState renderState, Camera camera, float partialTicks) {
        super.extract(renderState, camera, partialTicks);
        if (GENERAL.translucentEnchantmentGlyphParticles) {
            alpha = lifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks);
        }
    }

    @Override
    public int getLightCoords(float partialTick) {
        if (GENERAL.glowingEnchantmentGlyphParticles) {
            return PARTICLE_LIGHT_COLOR;
        }

        int lightColor = super.getLightCoords(partialTick);
        float f = (float) age / lifetime;
        f *= f;
        f *= f;
        int j = lightColor & 255;
        int k = lightColor >> 16 & 255;
        k += (int) (f * 15 * 16);
        if (k > PARTICLE_LIGHT_COLOR) {
            k = PARTICLE_LIGHT_COLOR;
        }

        return j | k << 16;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new RisingEnchantGlyphParticle(level, x, y, z, sprites.get(random));
        }
    }
}
