package einstein.subtle_effects.tickers.geyser_tickers;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import static einstein.subtle_effects.init.ModConfigs.ENVIRONMENT;
import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;

public class SmokeGeyserTicker extends GeyserTicker {

    public SmokeGeyserTicker(Level level, BlockPos pos, RandomSource random) {
        super(GeyserType.SMOKE, level, pos, random);
    }

    @Override
    protected void geyserTick() {
        if (age % 5 == 0) {
            Util.playClientSound(pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, ENVIRONMENT.flameGeyserSoundVolume.get(), random.nextFloat() * 0.7F + 0.3F);
        }

        for (int i = 0; i < 10; i++) {
            level.addParticle(ENVIRONMENT.useUpdatedSmoke ? ModParticles.SMOKE.get() : ParticleTypes.SMOKE,
                    pos.getX() + 0.5 + nextNonAbsDouble(random, 0.1),
                    pos.getY() + 1.0625,
                    pos.getZ() + 0.5 + nextNonAbsDouble(random, 0.1),
                    nextNonAbsDouble(random, 0.01, 0.05),
                    Mth.nextDouble(random, 0.2, 0.25),
                    nextNonAbsDouble(random, 0.01, 0.05)
            );
        }
    }
}
