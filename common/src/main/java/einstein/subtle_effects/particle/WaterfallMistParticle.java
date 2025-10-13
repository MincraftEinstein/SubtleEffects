package einstein.subtle_effects.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class WaterfallMistParticle extends BaseWaterfallParticle {

    protected WaterfallMistParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, new LifetimeAlpha(0.45F, 0, 0.8F, 1));
        friction = 0.90F;
        gravity = 0.1F;
        scale(20);
        pickSprite(sprites);

        if (random.nextBoolean()) {
            roll = 180 * Mth.DEG_TO_RAD;
            oRoll = roll;
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new WaterfallMistParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
