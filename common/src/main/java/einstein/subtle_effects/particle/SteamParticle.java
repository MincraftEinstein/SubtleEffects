package einstein.subtle_effects.particle;

import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SteamParticle extends SmokeParticle {

    protected SteamParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, 1, sprites);
        setColor(1, 1, 1);
        setAlpha(0.5F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();

        if (age >= (lifetime / 3) * 2) {
            alpha = Mth.clamp(alpha - 0.1F, 0, 1);
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SteamParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }

    public record FrostyBreathProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public @NotNull Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SteamParticle particle = new SteamParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
            particle.alpha = ModConfigs.ENTITIES.humanoids.frostyBreath.alpha.get();
            particle.gravity = 0;
            return particle;
        }
    }
}
