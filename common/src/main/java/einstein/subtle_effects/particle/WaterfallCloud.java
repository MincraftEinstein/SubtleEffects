package einstein.subtle_effects.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class WaterfallCloud extends BaseWaterfallParticle {

    private final SpriteSet sprites;

    protected WaterfallCloud(ClientLevel level, double x, double y, double z, double xSpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, 0, zSpeed, new LifetimeAlpha(0.7F, 0, 0.5F, 1));
        this.sprites = sprites;
//        roll = random.nextInt(); TODO should be a toggle config
//        oRoll = roll;
        gravity = 0.3F;
        speedUpWhenYMotionIsBlocked = true;
        scale(3);
        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new WaterfallCloud(level, x, y, z, xSpeed, zSpeed, sprites);
        }
    }
}
