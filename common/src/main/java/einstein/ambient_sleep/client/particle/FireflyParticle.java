package einstein.ambient_sleep.client.particle;

import einstein.ambient_sleep.util.LoopingSpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class FireflyParticle extends SuspendedParticle {

    private final LoopingSpriteSet sprites;

    public FireflyParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = (LoopingSpriteSet) sprites;
        hasPhysics = true;
        lifetime *= 2;
        setColor(1, 1, 1);
        setSprite(this.sprites.ambientSleep$nextSpite());
    }

    @Override
    public int getLightColor(float partialTick) {
        return 240 | super.getLightColor(partialTick) >> 16 & 0xFF << 16;
    }

    @Override
    public void tick() {
        super.tick();
        if (!removed) {
            setSprite(sprites.ambientSleep$nextSpite());
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            RandomSource random = level.getRandom();
            xSpeed = (random.nextGaussian() * 1.0E-6F) / 8;
            ySpeed = (random.nextGaussian() * 1.0E-6F) / 8;
            zSpeed = (random.nextGaussian() * 1.0E-6F) / 8;
            return new FireflyParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
