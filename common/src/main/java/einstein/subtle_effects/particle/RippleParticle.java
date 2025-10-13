package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.FloatParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class RippleParticle extends FlatPlaneParticle {

    private final SpriteSet sprites;
    private final boolean translucent;

    protected RippleParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, boolean translucent, float scale) {
        super(level, x, y, z, sprites.first());
        this.sprites = sprites;
        this.translucent = translucent;
        rotation.rotateX(90 * Mth.DEG_TO_RAD);
        renderBackFace = false;
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
    protected Layer getLayer() {
        if (translucent) {
            return Layer.TRANSLUCENT;
        }
        return Layer.OPAQUE;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<FloatParticleOptions> {

        @Override
        public Particle createParticle(FloatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new RippleParticle(level, x, y, z, sprites, true, options.f());
        }
    }

    public record LavaProvider(SpriteSet sprites) implements ParticleProvider<FloatParticleOptions> {

        @Override
        public Particle createParticle(FloatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            RippleParticle particle = new RippleParticle(level, x, y, z, sprites, false, options.f());
            particle.setColor(0.871F, 0.478F, 0.133F);
            return particle;
        }
    }
}
