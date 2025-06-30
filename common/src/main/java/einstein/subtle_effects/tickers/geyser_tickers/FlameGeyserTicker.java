package einstein.subtle_effects.tickers.geyser_tickers;

import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;

import static einstein.subtle_effects.init.ModConfigs.ENVIRONMENT;
import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;

public class FlameGeyserTicker extends GeyserTicker {

    public FlameGeyserTicker(Level level, BlockPos pos, RandomSource random) {
        super(GeyserType.FLAME, level, pos, random);
    }

    @Override
    protected ParticleOptions getParticle() {
        return level.getBiome(pos).is(Biomes.SOUL_SAND_VALLEY) ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;
    }

    @Override
    protected void geyserTick() {
        if (age % 5 == 0) {
            Util.playClientSound(pos, ModSounds.GEYSER_WHOOSH.get(), SoundSource.BLOCKS, ENVIRONMENT.flameGeyserSoundVolume.get(), random.nextFloat() * 0.7F + 0.3F);
        }

        for (int i = 0; i < 10; i++) {
            level.addParticle(getParticle(),
                    pos.getX() + 0.5 + nextNonAbsDouble(random, 0.1),
                    pos.getY() + 1.0625,
                    pos.getZ() + 0.5 + nextNonAbsDouble(random, 0.1),
                    nextNonAbsDouble(random, 0.01, 0.05),
                    Mth.nextDouble(random, 0.3, 0.35),
                    nextNonAbsDouble(random, 0.01, 0.05)
            );
        }
    }
}
