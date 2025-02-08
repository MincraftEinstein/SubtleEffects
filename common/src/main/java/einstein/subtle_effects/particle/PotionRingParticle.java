package einstein.subtle_effects.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class PotionRingParticle extends FlatPlaneParticle {

    protected PotionRingParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        hasPhysics = false;
        speedUpWhenYMotionIsBlocked = false;
        lifetime = 10;
        alpha = 0;
        quadSize = 0.2F;
        rotation = new Vector3f(-90, 90 * random.nextInt(3), 0).mul(Mth.DEG_TO_RAD);
        pickSprite(sprites);
        scale(3);
    }

    @Override
    public void tick() {
        super.tick();
        float halfLife = lifetime / 2F;
        int ageMultiplier = age > halfLife ? -1 : 1;

        yd += (0.25 / halfLife) * ageMultiplier;
        alpha += 0.2F * ageMultiplier;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<ColorParticleOption> {

        @Override
        public Particle createParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            PotionRingParticle particle = new PotionRingParticle(level, x, y, z, sprites);
            particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
            return particle;
        }
    }
}
