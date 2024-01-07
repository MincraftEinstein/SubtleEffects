package einstein.ambient_sleep.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class SparkParticle extends BaseAshSmokeParticle {

    protected SparkParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float lifeTimeModifier, SpriteSet sprites) {
        super(level, x, y, z, 0.1F, 0.1F, 0.1F, xSpeed, ySpeed, zSpeed, level.random.nextInt(11) / lifeTimeModifier, sprites, 0, 20, -0.1F, true);
        quadSize = 0.1F * (random.nextFloat() * 0.5F + 0.5F) * 2.0F * 0.75F * random.nextInt(11) / 10;
        setColor(1, 1, 1);
    }

    @Override
    public int getLightColor(float partialTick) {
        return 240 | super.getLightColor(partialTick) >> 16 & 0xFF << 16;
    }

    public record LongLifeProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 10, sprites);
        }
    }

    public record ShortLifeProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 20, sprites);
        }
    }
}
