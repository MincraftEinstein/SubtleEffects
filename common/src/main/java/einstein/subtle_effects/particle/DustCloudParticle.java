package einstein.subtle_effects.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class DustCloudParticle extends SingleQuadParticle {

    private final SpriteSet sprites;
    private final double ySpeed;
    private final Minecraft minecraft = Minecraft.getInstance();

    protected DustCloudParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int maxLifeTime, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, 0, zSpeed);
        this.sprites = sprites;
        this.ySpeed = ySpeed;
        gravity = 0.1F;
        lifetime = Math.max(random.nextInt(maxLifeTime), maxLifeTime - 10);
        speedUpWhenYMotionIsBlocked = true;
        alpha = ENTITIES.dustClouds.alpha.get();
        setSpriteFromAge(sprites);
        scale(3 * ENTITIES.dustClouds.scale.get());
        setSize(0.25F, 0.25F);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        if (ENTITIES.dustClouds.lessViewBlocking && minecraft.options.getCameraType().isFirstPerson() && camera.getEntity().distanceToSqr(x, y, z) < 4) {
            return;
        }
        super.render(consumer, camera, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);

        if (age >= (lifetime / 3) * 2) {
            yd *= ySpeed / (random.nextInt(3, 7) * 1000);
            alpha -= 0.1F;
        }

        if (onGround || alpha <= 0) {
            remove();
        }
    }

    public record SmallProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource randomSource) {
            return new DustCloudParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 25, sprites);
        }
    }

    public record LargeProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource randomSource) {
            return new DustCloudParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 35, sprites);
        }
    }
}
