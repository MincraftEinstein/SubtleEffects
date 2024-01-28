package einstein.ambient_sleep.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SuspendedTownParticle;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class AllayMagicParticle extends SuspendedTownParticle {

    public AllayMagicParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        float multiplier = random.nextFloat() * 0.4F + 0.6F;
        setColor(randomizeColor(0.133F, multiplier), randomizeColor(0.812F, multiplier), randomizeColor(1, multiplier));
        pickSprite(sprites);
    }

    protected float randomizeColor(float color, float multiplier) {
        return (random.nextFloat() * 0.2F + 0.8F) * color * multiplier;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 240 | super.getLightColor(partialTick) >> 16 & 0xFF << 16;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new AllayMagicParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
