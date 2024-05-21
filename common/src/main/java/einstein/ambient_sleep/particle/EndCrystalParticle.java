package einstein.ambient_sleep.particle;

import einstein.ambient_sleep.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class EndCrystalParticle extends SuspendedParticle {

    public EndCrystalParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z) {
        super(level, sprites, x, y, z, 0, 0, 0);
        quadSize /= 2;
        xd /= 6;
        yd /= 4;
        zd /= 6;
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
            return new EndCrystalParticle(level, sprites, x, y, z);
        }
    }
}
