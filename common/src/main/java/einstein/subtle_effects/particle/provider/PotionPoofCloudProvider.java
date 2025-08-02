package einstein.subtle_effects.particle.provider;

import einstein.subtle_effects.particle.option.ColorParticleOptions;
import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.particle.SpriteSet;
import org.joml.Vector3f;

public record PotionPoofCloudProvider(SpriteSet sprites) implements ParticleProvider<ColorParticleOptions> {

    @Override
    public Particle createParticle(ColorParticleOptions option, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Particle particle = new SpellParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        Vector3f color = option.getColor();
        particle.setColor(color.x(),  color.y(),  color.z());
        particle.scale(3.5F);
        ((ParticleAccessor) particle).setHasPhysics(true);
        return particle;
    }
}
