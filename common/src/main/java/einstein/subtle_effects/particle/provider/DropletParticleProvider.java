package einstein.subtle_effects.particle.provider;

import einstein.subtle_effects.particle.option.FloatParticleOptions;
import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public record DropletParticleProvider(SpriteSet sprites) implements ParticleProvider<FloatParticleOptions> {

    @Override
    public Particle createParticle(FloatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        TextureSheetParticle particle = DripParticle.createWaterFallParticle(null, level, x, y, z, xSpeed, ySpeed, zSpeed);
        setupParticle(sprites, options, xSpeed, ySpeed, zSpeed, particle);
        return particle;
    }

    private static void setupParticle(SpriteSet sprites, FloatParticleOptions options, double xSpeed, double ySpeed, double zSpeed, TextureSheetParticle particle) {
        particle.pickSprite(sprites);
        particle.setColor(1, 1, 1);
        particle.setParticleSpeed(xSpeed, ySpeed, zSpeed);
        particle.scale(options.f() * 1.5F);
        ((ParticleAccessor) particle).setGravity(0.06F * options.f());
    }

    public record LavaProvider(SpriteSet sprites) implements ParticleProvider<FloatParticleOptions> {

        @Override
        public Particle createParticle(FloatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TextureSheetParticle particle = DripParticle.createLavaFallParticle(null, level, x, y, z, xSpeed, ySpeed, zSpeed);
            setupParticle(sprites, options, xSpeed, ySpeed, zSpeed, particle);
            return particle;
        }
    }
}
