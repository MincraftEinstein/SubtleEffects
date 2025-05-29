package einstein.subtle_effects.tickers;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.particle.option.IntegerParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;

public class FlameGeyserTicker extends Ticker {

    public static final List<BlockPos> ACTIVE_GEYSERS = new ArrayList<>();
    public static final List<BlockPos> INACTIVE_GEYSERS = new ArrayList<>();
    public static final List<Block> VALID_BLOCKS = List.of(Blocks.NETHERRACK, Blocks.BLACKSTONE, Blocks.SOUL_SOIL);

    private final ParticleOptions particle;
    private final Level level;
    private final BlockPos pos;
    private final RandomSource random;
    private final int lifeTime;
    private int age;

    public FlameGeyserTicker(Level level, BlockPos pos, RandomSource random) {
        this.level = level;
        this.pos = pos;
        this.random = random;
        particle = level.getBiome(pos).is(Biomes.SOUL_SAND_VALLEY) ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;
        lifeTime = Mth.nextInt(random, 100, 300);
        ACTIVE_GEYSERS.add(pos);
    }

    @Override
    public void tick() {
        age++;

        if (age >= lifeTime) {
            remove();
            return;
        }

        if (Util.isChunkLoaded(level, pos.getX(), pos.getZ())) {
            if (checkLocation(level, pos, false)) {
                if (age == 1) {
                    level.addParticle(new IntegerParticleOptions(ModParticles.GEYSER_HOLE.get(), lifeTime),
                            pos.getX() + 0.5,
                            pos.getY() + 1.001,
                            pos.getZ() + 0.5,
                            0, 0, 0
                    );
                }

                if (age % 5 == 0) {
                    Util.playClientSound(pos, ModSounds.GEYSER_WHOOSH.get(), SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.7F + 0.3F);
                }

                for (int i = 0; i < 10; i++) {
                    level.addParticle(particle,
                            pos.getX() + 0.5 + nextNonAbsDouble(random, 0.1),
                            pos.getY() + 1.0625,
                            pos.getZ() + 0.5 + nextNonAbsDouble(random, 0.1),
                            nextNonAbsDouble(random, 0.01, 0.05),
                            Mth.nextDouble(random, 0.3, 0.35),
                            nextNonAbsDouble(random, 0.01, 0.05)
                    );
                }
                return;
            }
        }
        remove();
    }

    public static boolean checkLocation(Level level, BlockPos pos, boolean checkAbove) {
        if (VALID_BLOCKS.contains(level.getBlockState(pos).getBlock())) {
            BlockPos abovePos = pos.above();
            if (isNotFaceSturdyOrFluidEmpty(level, abovePos)) {
                if (checkAbove) {
                    for (int i = 1; i < 5; i++) {
                        if (!isNotFaceSturdyOrFluidEmpty(level, abovePos.relative(Direction.UP, i))) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isNotFaceSturdyOrFluidEmpty(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return !state.isFaceSturdy(level, pos, Direction.DOWN) && !(state.getBlock() instanceof BaseFireBlock) && level.getFluidState(pos).isEmpty();
    }

    @Override
    public void remove() {
        super.remove();
        ACTIVE_GEYSERS.remove(pos);
        INACTIVE_GEYSERS.add(pos);
        TickerManager.schedule(Mth.nextInt(random, 100, 500), () -> INACTIVE_GEYSERS.remove(pos));
    }
}
