package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.FloatParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class ExperienceParticle extends TextureSheetParticle {

    protected ExperienceParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        pickSprite(sprites);
        yd *= 0.2F;

        if (xSpeed == 0 && zSpeed == 0) {
            xd *= 0.1F;
            zd *= 0.1F;
        }

        quadSize *= 0.3F;
        lifetime = (int) (8 / (random.nextDouble() * 0.8 + 0.2));
        hasPhysics = true;
        alpha = 0.5F;
        gravity = 0.3F;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        float f = (age + partialTicks) / 2;
        rCol = (Mth.sin(f + 0) + 1) * 0.5F;
        gCol = 1;
        bCol = (Mth.sin(f + 4.1887903F) + 1) * 0.1F;
        super.render(buffer, camera, partialTicks);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<FloatParticleOptions> {

        @Override
        public Particle createParticle(FloatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            ExperienceParticle particle = new ExperienceParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.get(random));
            particle.setPower(options.f());
            return particle;
        }
    }
}
