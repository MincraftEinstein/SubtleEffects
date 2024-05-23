package einstein.subtle_effects.particle;

import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SuspendedParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class InsectParticle extends SuspendedParticle {

    private final ParticleAnimation animation;
    private final boolean glowing;

    public InsectParticle(ClientLevel level, ParticleAnimation animation, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, boolean glowing) {
        super(level, animation.getSprites(), x, y, z, xSpeed, ySpeed, zSpeed);
        this.animation = animation;
        this.glowing = glowing;
        hasPhysics = true;
        lifetime = animation.getAnimationLifetime();
        setColor(1, 1, 1);
        updateSprite();
    }

    @Override
    public int getLightColor(float partialTick) {
        return glowing ? Util.getLightColor(super.getLightColor(partialTick)) : super.getLightColor(partialTick);
    }

    @Override
    public void tick() {
        super.tick();
        if (!removed) {
            animation.tick();
            updateSprite();
        }
    }

    private void updateSprite() {
        if (!removed) {
            setSprite(animation.getSpriteForFrame());
        }
    }

    public record Provider(Supplier<ParticleAnimation> animation, boolean glowing) implements ParticleProvider<SimpleParticleType>{

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            RandomSource random = level.getRandom();
            xSpeed = (random.nextGaussian() * 1.0E-6F) / 8;
            ySpeed = (random.nextGaussian() * 1.0E-6F) / 8;
            zSpeed = (random.nextGaussian() * 1.0E-6F) / 8;
            return new InsectParticle(level, animation.get(), x, y, z, xSpeed, ySpeed, zSpeed, glowing);
        }
    }
}
