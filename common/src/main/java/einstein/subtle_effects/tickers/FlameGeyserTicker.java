package einstein.subtle_effects.tickers;

import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;

public class FlameGeyserTicker extends Ticker {

    public static final List<BlockPos> ACTIVE_GEYSERS = new ArrayList<>();
    public static final List<BlockPos> INACTIVE_GEYSERS = new ArrayList<>();

    private final ParticleOptions particle;
    private final Level level;
    private final BlockPos pos;
    private final RandomSource random;
    private final BlockPos abovePos;
    private int age;

    public FlameGeyserTicker(Level level, BlockPos pos, RandomSource random) {
        this.level = level;
        this.pos = pos;
        this.random = random;
        abovePos = pos.above();
        particle = level.getBiome(pos).is(Biomes.SOUL_SAND_VALLEY) ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;
        ACTIVE_GEYSERS.add(pos);
    }

    // TODO
    //  - sounds needed
    //  - just before the geyser starts a dark spot particle should appear on the block to indicate it and fade out after finished

    @Override
    public void tick() {
        age++;

        if (age >= 200) {
            remove();
            INACTIVE_GEYSERS.add(pos);
            TickerManager.schedule(200, () -> INACTIVE_GEYSERS.remove(pos));
            return;
        }

        if (Util.isChunkLoaded(level, pos.getX(), pos.getZ())) {
            if (level.getBlockState(pos).is(Blocks.NETHERRACK)) {
                if (!level.getBlockState(abovePos).isFaceSturdy(level, pos, Direction.DOWN) && level.getFluidState(abovePos).isEmpty()) {
                    if (age % 5 == 0) {
                        // TODO custom sound event to change subtitles
                        Util.playClientSound(pos, SoundEvents.GHAST_SHOOT, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.7F + 0.3F);
                    }

                    for (int i = 0; i < 10; i++) {
                        level.addParticle(particle,
                                pos.getX() + 0.5,
                                pos.getY() + 1.0625,
                                pos.getZ() + 0.5,
                                nextNonAbsDouble(random, 0.01, 0.05),
                                Mth.nextDouble(random, 0.3, 0.35),
                                nextNonAbsDouble(random, 0.01, 0.05)
                        );
                    }
                    return;
                }
            }
        }
        remove();
    }

    @Override
    public void remove() {
        super.remove();
        ACTIVE_GEYSERS.remove(pos);
    }
}
