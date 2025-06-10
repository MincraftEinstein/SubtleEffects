package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.BooleanParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

import static einstein.subtle_effects.util.Util.setRandomizedColor;

public class AllayMagicParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    public AllayMagicParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        friction = 0.96F;
        gravity = -0.1F;
        xd = (xd * 0.1) + xSpeed;
        yd = (yd * 0.1) + ySpeed;
        zd = (zd * 0.1) + zSpeed;
        quadSize *= 0.75F * 0.8F;
        lifetime = random.nextIntBetweenInclusive(20, 35);
        hasPhysics = false;
        speedUpWhenYMotionIsBlocked = true;
        setRandomizedColor(this, random, 0.133F, 0.812F, 1);
        setSpriteFromAge(sprites);
        setAlpha(Mth.clamp(random.nextFloat(), 0.5F, 1));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return quadSize * Mth.clamp((age + partialTicks) / lifetime * 32, 0, 1);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return Util.PARTICLE_LIGHT_COLOR;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new AllayMagicParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }

    public record VexProvider(SpriteSet sprites) implements ParticleProvider<BooleanParticleOptions> {

        @Override
        public Particle createParticle(BooleanParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            AllayMagicParticle particle = new AllayMagicParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
            if (type.bool()) {
                setRandomizedColor(particle, particle.random, 0.859F, 0.478F, 0.588F);
                return particle;
            }
            setRandomizedColor(particle, particle.random, 0.635F, 0.737F, 0.835F);
            return particle;
        }
    }
}
