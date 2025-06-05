package einstein.subtle_effects.particle.provider;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class SneezeParticle extends TextureSheetParticle {

    private final LifetimeAlpha lifetimeAlpha = new LifetimeAlpha(0.4F, 0, 0.5F, 1);
    private final SpriteSet sprites;

    protected SneezeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        xd = xSpeed;
        yd = ySpeed;
        zd = zSpeed;

        // Not sure which is the vanilla color, so 37CD87 was used
        // 37CD87. The result of subtracting the vanilla provided color components (200, 50, 120) from 255
        // 37C985. Color picked from a screenshot
        float multiplier = random.nextFloat() * 0.4F + 0.6F;
        rCol = Util.randomizeColor(random, 0.21F, multiplier);
        gCol = Util.randomizeColor(random, 0.8F, multiplier);
        bCol = Util.randomizeColor(random, 0.52F, multiplier);
        alpha = lifetimeAlpha.startAlpha();
        friction = 0.98F;
        this.sprites = sprites;
        setSpriteFromAge(sprites);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        alpha = lifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks);
        super.render(consumer, camera, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SneezeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}