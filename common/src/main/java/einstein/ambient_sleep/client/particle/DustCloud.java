package einstein.ambient_sleep.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class DustCloud extends TextureSheetParticle {

    private final SpriteSet sprites;
    private final double ySpeed;

    protected DustCloud(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, 0, zSpeed);
        this.sprites = sprites;
        this.ySpeed = ySpeed;
        gravity = 0.1F;
        yd = 0;
        int maxLifeTime = 25;
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

        if (age == ((lifetime / 3) * 2)) {
            yd *= ySpeed;
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DustCloud(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
