package einstein.subtle_effects.particle;

import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.util.BubbleSetter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class DrowningBubbleParticle extends BubbleParticle {

    public DrowningBubbleParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        alpha = ENTITIES.humanoids.drowningBubbleAlpha.get();
        lifetime *= 3;

        if (ENTITIES.humanoids.forceDrowningBubblesToColumn) {
            xd = 0;
            yd = 0;
            zd = 0;
        }
    }

    @Override
    public Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DrowningBubbleParticle particle = new DrowningBubbleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprites);

            if (BCWPPackManager.isPackLoaded()) {
                ((BubbleSetter) particle).subtleEffects$setupBubble(sprites, false);
            }
            return particle;
        }
    }
}
