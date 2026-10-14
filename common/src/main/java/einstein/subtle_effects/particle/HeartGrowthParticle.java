package einstein.subtle_effects.particle;

import einstein.subtle_effects.mixin.client.particle.SingleQuadParticleAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public class HeartGrowthParticle extends HeartParticle {

    private final SpriteSet sprites;

    public HeartGrowthParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        this.sprites = sprites;
        lifetime = 12;
        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);

        if (age == lifetime - 1) {
            Minecraft minecraft = Minecraft.getInstance();
            // Not using 'createParticle' as that would create recursion from the heart particle being replaced
            Particle particle = minecraft.particleEngine.makeParticle(ParticleTypes.HEART, x, y, z, 0, 0, 0);
            if (particle != null) {
                particle.setParticleSpeed(xd, yd, zd);
                ((SingleQuadParticleAccessor) particle).setQuadSize(quadSize);
                minecraft.particleEngine.add(particle);
            }
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new HeartGrowthParticle(level, x, y, z, sprites);
        }
    }
}
