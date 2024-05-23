package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.SheepFluffParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class FeatherParticle extends TextureSheetParticle {

    protected FeatherParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        quadSize = 0.1F;
        gravity = 0.35F;
        lifetime = 30;
        setSize(0.1F, 0.1F);
        pickSprite(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        oRoll = roll;
        if (!onGround) {
            roll = oRoll + 0.1F * -(age / 10F);
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FeatherParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }

    public record SheepFluffProvider(SpriteSet sprites) implements ParticleProvider<SheepFluffParticleOptions> {

        @Nullable
        @Override
        public Particle createParticle(SheepFluffParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FeatherParticle particle = new FeatherParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
            Vector3f color = type.getColor();
            particle.setColor(color.x(), color.y(), color.z());
            particle.gravity = 0.5F;
            return particle;
        }
    }
}
