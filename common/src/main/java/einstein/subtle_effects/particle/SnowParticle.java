package einstein.subtle_effects.particle;

import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluids;

public class SnowParticle extends SingleQuadParticle {

    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

    protected SnowParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
        lifetime = 20;
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
            ((ParticleAccessor) particle).setGravity(1);
            return particle;
        }
    }

    public record SnowballTrailProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        private static final float GRAVITY = 0.1F;

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            SnowParticle particle = new SnowParticle(level, x, y, z, 0, 0, 0, sprites.get(random));
            ((ParticleAccessor) particle).setGravity(GRAVITY);
            particle.setParticleSpeed(0, -GRAVITY, 0);
            return particle;
        }
    }

    public record FreezingProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            SnowParticle particle = new SnowParticle(level, x, y, z, 0, 0, 0, sprites.get(random));
            ((ParticleAccessor) particle).setGravity(0.05F);
            particle.setParticleSpeed(0, -0.05, 0);
            return particle;
        }
    }
}
