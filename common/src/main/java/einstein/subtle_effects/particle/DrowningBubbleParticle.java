package einstein.subtle_effects.particle;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.BubbleSetter;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class DrowningBubbleParticle extends BubbleParticle {

    public DrowningBubbleParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        alpha = ModConfigs.ENTITIES.humanoids.drowningBubbleAlpha.get();
        lifetime *= 3;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DrowningBubbleParticle particle = new DrowningBubbleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprites);

            if (Util.isBCWPPackLoaded()) {
                ((BubbleSetter) particle).subtleEffects$setupBubble(sprites, false);
            }
            return particle;
        }
    }
}
