package einstein.subtle_effects.particle;

import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class EndPortalParticle extends GlowingSuspendedParticle {

    private static final int[] COLORS = {
            0xFF49FFAA,
            0xFF1F8888,
            0xFF3FBCF8,
            0xFF2981C5,
            0xFF807BB5,
            0xFF4D4C6B
    };

    public EndPortalParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
        Util.setColorFromHex(this, COLORS[random.nextInt(COLORS.length)]);
        xd = -(xd / 6);
        yd = -(yd / 4);
        zd = -(zd / 6);
    }

    @Override
    public Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new EndPortalParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
