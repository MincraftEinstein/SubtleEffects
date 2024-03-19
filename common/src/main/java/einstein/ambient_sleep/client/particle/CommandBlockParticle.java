package einstein.ambient_sleep.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.EnchantmentTableParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class CommandBlockParticle extends EnchantmentTableParticle {

    public CommandBlockParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        quadSize = 0.1F;
        float shade = Mth.clamp(level.random.nextFloat(), 0.6F, 1);
        setColor(shade, shade, shade);
        pickSprite(sprites);
    }

    @Override
    public int getLightColor(float partialTick) {
        return 240 | super.getLightColor(partialTick) >> 16 & 0xFF << 16;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new CommandBlockParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
