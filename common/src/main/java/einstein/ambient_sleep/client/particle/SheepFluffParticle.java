package einstein.ambient_sleep.client.particle;

import einstein.ambient_sleep.client.particle.option.SheepFluffParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class SheepFluffParticle extends FeatherParticle {

    protected SheepFluffParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites, Vector3f color) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        setColor(color.x(), color.y(), color.z());
        gravity = 0.5F;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SheepFluffParticleOptions> {

        @Nullable
        @Override
        public Particle createParticle(SheepFluffParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SheepFluffParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites, type.getColor());
        }
    }
}
