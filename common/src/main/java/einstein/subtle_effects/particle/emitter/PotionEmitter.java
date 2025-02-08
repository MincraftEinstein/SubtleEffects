package einstein.subtle_effects.particle.emitter;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.mixin.client.particle.ColorParticleOptionAccessor;
import einstein.subtle_effects.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ColorParticleOption;

public class PotionEmitter extends NoRenderParticle {

    private final int color;

    protected PotionEmitter(ClientLevel level, double x, double y, double z, int color) {
        super(level, x, y, z);
        this.color = color;
        lifetime = 0;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 3; i++) {
            level.addParticle(ColorParticleOption.create(ModParticles.POTION_RING.get(), color),
                    x, y - 0.1 + (0.4 * i), z,
                    0, 0, 0
            );
        }

        for (int i = 0; i < 20; i++) {
            level.addParticle(ColorParticleOption.create(ModParticles.POTION_DOT.get(), color),
                    x + MathUtil.nextNonAbsDouble(random, 0.75),
                    y + random.nextDouble(),
                    z + MathUtil.nextNonAbsDouble(random, 0.75),
                    0, 0.1, 0
            );
        }

        if (age++ >= lifetime) {
            remove();
        }
    }

    public record Provider() implements ParticleProvider<ColorParticleOption> {

        @Override
        public Particle createParticle(ColorParticleOption option, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PotionEmitter(level, x, y, z, ((ColorParticleOptionAccessor) option).getColor());
        }
    }
}
