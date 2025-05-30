package einstein.subtle_effects.particle.provider;

import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class TerrainNoMomentumParticleProvider extends TerrainParticle.Provider {

    @Nullable
    @Override
    public Particle createParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Particle particle = super.createParticle(options, level, x, y, z, xSpeed, ySpeed, zSpeed);
        if (particle != null) {
            particle.setParticleSpeed(0, 0, 0);

            float minGravity = 0.5F;
            float maxGravity = 1.5F;

            if (options.getState().is(Blocks.HONEY_BLOCK)) {
                minGravity = 0;
                maxGravity = 0.2F;
            }

            ((ParticleAccessor) particle).setGravity(Mth.nextFloat(level.random, minGravity, maxGravity));
            return particle;
        }
        return null;
    }
}
