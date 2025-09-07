package einstein.subtle_effects.particle;

import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;

public class LargeCascadeDroplet extends BaseWaterfallParticle {

    protected LargeCascadeDroplet(ClientLevel level, double x, double y, double z, double xSpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, 0, zSpeed, LifetimeAlpha.ALWAYS_OPAQUE);
        yd /= 2;
        friction = 0.90F;
        gravity = 0.3F;
        alpha = 0.7F;
        pickSprite(sprites);
        Util.setColorFromHex(this, level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor());
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new LargeCascadeDroplet(level, x, y, z, xSpeed, zSpeed, sprites);
        }
    }
}
