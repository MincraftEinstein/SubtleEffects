package einstein.subtle_effects.particle;

import einstein.subtle_effects.util.MathUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

public class FireflyParticle extends BaseAnimatedParticle {

    private int xTicks;
    private int xTicksMax = random.nextInt(60);
    private int waitTicksMax = random.nextInt(20, 40);
    private int waitTicks = waitTicksMax;
    private boolean isMovingUp;
    private int yTicks = 20;
    private int zTicks;
    private int zTicksMax = random.nextInt(60);

    public FireflyParticle(ClientLevel level, ParticleAnimation animation, double x, double y, double z) {
        super(level, x, y, z, 0, 0, 0, animation); // DON'T replace with the other constructor
        setSize(0.02F, 0.02F);
        hasPhysics = true;
        friction = 1;
        gravity = 0;
        quadSize = 0.3F;
    }

    @Override
    public void tick() {
        super.tick();

        if (removed) {
            return;
        }

        if (x == xo || onGround || z == zo) {
            remove();
            return;
        }

        xTicks++;
        if (xTicks >= xTicksMax) {
            xTicks = 0;
            xTicksMax = random.nextInt(60);
            xd = MathUtil.nextNonAbsDouble(random, 0.1);
        }

        waitTicks++;
        if (waitTicks >= waitTicksMax) {
            if (!isMovingUp) {
                yd = MathUtil.nextNonAbsDouble(random, 0.05);
                isMovingUp = true;
            }

            yTicks--;
            if (yTicks <= 0) {
                isMovingUp = false;
                yd = 0;
                yTicks = 0;
                waitTicks = 0;
                waitTicksMax = random.nextInt(20, 40);
            }
        }

        zTicks++;
        if (zTicks >= zTicksMax) {
            zTicks = 0;
            zTicksMax = random.nextInt(60);
            zd = MathUtil.nextNonAbsDouble(random, 0.1);
        }
    }

    @Override
    public int getLightColor(float partialTick) {
        return Util.PARTICLE_LIGHT_COLOR;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public @NotNull Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new FireflyParticle(level, new ParticleAnimation(sprites, 16, 3), x, y, z);
        }
    }
}
