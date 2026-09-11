package einstein.subtle_effects.particle;

import einstein.subtle_effects.util.MathUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.util.Mth.*;

public class EndCrystalMagicParticle extends GlowingSuspendedParticle {

    private final Vec3 pos;
    private final double offset = nextDouble(random, 1, 2);
    private final float speed = nextFloat(random, 0.2F, 1);
    private float yaw;

    public EndCrystalMagicParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z) {
        super(level, sprites, 0, y, 0, 0, 0, 0);
        pos = new Vec3(x, y, z);
        yaw = nextFloat(random, 0, 360);
        lifetime *= 2;
        xd = 0;
        yd = MathUtil.nextDouble(random, 0.012);
        zd = 0;
        move(0, yd, 0);
        xo = this.x;
        yo = this.y;
        zo = this.z;
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;

        if (age++ >= lifetime) {
            remove();
            return;
        }

        yaw += speed;
        if (yaw >= 360) {
            yaw -= 360;
        }

        move(0, yd, 0);
    }

    @Override
    public void move(double xd, double yd, double zd) {
        Vec3 rotatedOffset = new Vec3(offset, 0, 0).yRot(yaw * DEG_TO_RAD);
        setPos(pos.x() + rotatedOffset.x(), y + yd, pos.z() + rotatedOffset.z());
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new EndCrystalMagicParticle(level, sprites, x, y, z);
        }
    }
}
