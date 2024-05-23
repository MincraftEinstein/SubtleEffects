package einstein.subtle_effects.particle;

import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class EndPortalParticle extends SuspendedParticle {

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
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return Util.getLightColor(super.getLightColor(partialTick));
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new EndPortalParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
