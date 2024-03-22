package einstein.ambient_sleep.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class DustCloudParticle extends TextureSheetParticle {

    private final SpriteSet sprites;
    private final double ySpeed;

    protected DustCloudParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int maxLifeTime, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, 0, zSpeed);
        this.sprites = sprites;
        this.ySpeed = ySpeed;
        gravity = 0.1F;
        lifetime = Math.max(random.nextInt(maxLifeTime), maxLifeTime - 10);
        speedUpWhenYMotionIsBlocked = true;
        setSpriteFromAge(sprites);
        scale(3);
        setSize(0.25F, 0.25F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);

        if (age >= (lifetime / 3) * 2) {
            yd *= ySpeed / (random.nextInt(3, 7) * 1000);
        }

        if (onGround) {
            remove();
        }
    }

    public record SmallProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DustCloudParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 25, sprites);
        }
    }

    public record LargeProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DustCloudParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 35, sprites);
        }
    }
}
