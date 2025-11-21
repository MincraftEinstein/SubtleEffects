package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.FloatParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;

public class RippleParticle extends FlatPlaneParticle {

    private final SpriteSet sprites;
    private final boolean translucent;

    protected RippleParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, boolean translucent, float scale) {
        super(level, x, y, z);
        this.sprites = sprites;
        this.translucent = translucent;
        rotation.rotateX(90 * Mth.DEG_TO_RAD);
        setSpriteFromAge(sprites);
        scale(scale);
        lifetime = 5;

        if (translucent) {
            alpha = 0.2F;
        }
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        if (translucent) {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<FloatParticleOptions> {

        @Override
        public Particle createParticle(FloatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new RippleParticle(level, x, y, z, sprites, true, options.f());
        }
    }

    public record LavaProvider(SpriteSet sprites) implements ParticleProvider<FloatParticleOptions> {

        @Override
        public Particle createParticle(FloatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle particle = new RippleParticle(level, x, y, z, sprites, false, options.f());
            particle.setColor(0.871F, 0.478F, 0.133F);
            return particle;
        }
    }
}
