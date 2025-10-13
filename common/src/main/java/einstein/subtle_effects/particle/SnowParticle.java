package einstein.subtle_effects.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluids;

public class SnowParticle extends SingleQuadParticle {

    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

    protected SnowParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        lifetime = 20;
        pickSprite(sprites);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();

        pos.set(x, y, z);
        oRoll = roll;
        roll = oRoll + 0.1F;

        if (onGround || level.getFluidState(pos).is(Fluids.WATER)) {
            remove();
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            SnowParticle particle = new SnowParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.get(random));
            particle.gravity = 1;
            return particle;
        }
    }

    public record SnowballTrailProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        private static final float GRAVITY = 0.1F;

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            SnowParticle particle = new SnowParticle(level, x, y, z, 0, 0, 0, sprites.get(random));
            particle.gravity = GRAVITY;
            particle.xd = 0;
            particle.yd = -GRAVITY;
            particle.zd = 0;
            return particle;
        }
    }

    public record FreezingProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            SnowParticle particle = new SnowParticle(level, x, y, z, 0, 0, 0, sprites.get(random));
            particle.gravity = 0.05F;
            particle.xd = 0;
            particle.yd = -0.05F;
            particle.zd = 0;
            return particle;
        }
    }
}
