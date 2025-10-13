package einstein.subtle_effects.particle;

import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class DrowningBubblePopParticle extends BubblePopParticle {

    public DrowningBubblePopParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        alpha = ModConfigs.ENTITIES.humanoids.drowningBubbleAlpha.get();
    }

    @Override
    public Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DrowningBubblePopParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
