package einstein.subtle_effects.tickers.geyser_tickers;

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

public class BubbleGeyserTicker extends GeyserTicker {

    public BubbleGeyserTicker(Level level, BlockPos pos, RandomSource random) {
        super(GeyserType.BUBBLE, level, pos, random);
    }

    @Override
    protected void geyserTick() {
        if (age % 15 == 0) {
            Util.playClientSound(pos, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, ENVIRONMENT.geysers.bubbleGeyserSoundVolume.get(), 0.9F + random.nextFloat() * 0.15F);
        }

        if (random.nextBoolean()) {
            level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP,
                    pos.getX() + 0.5 + nextNonAbsDouble(random, 0.1),
                    pos.getY() + 1.1,
                    pos.getZ() + 0.5 + nextNonAbsDouble(random, 0.1),
                    nextNonAbsDouble(random, 0.01, 0.05),
                    0,
                    nextNonAbsDouble(random, 0.01, 0.05)
            );
        }
    }
}
