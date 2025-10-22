package einstein.subtle_effects.ticking;

import einstein.subtle_effects.data.SparkProviderData;
import einstein.subtle_effects.data.SparkProviderReloadListener;
import einstein.subtle_effects.mixin.common.block.AbstractCandleBlockAccessor;
import einstein.subtle_effects.mixin.common.block.BaseFireBlockAccessor;
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.util.Box;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.SparkType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static einstein.subtle_effects.util.MathUtil.*;

public class SparkProviderManager {

    public static void tick(Level level, RandomSource random, Block block, BlockState state, BlockPos pos) {
        List<SparkProviderReloadListener.SparkProvider> providers = SparkProviderReloadListener.PROVIDERS.get(block);
        if (providers != null && !providers.isEmpty()) {
            providers.forEach(provider -> {
                SparkProviderReloadListener.BlockStateHolder stateHolder = provider.stateHolder();
                if (!stateHolder.matches(state)) {
                    return;
                }

                SparkProviderData.Options options = provider.options();
                SparkProviderData.PresetType preset = options.preset();
                List<Integer> colors = options.colors().orElse(SparkParticle.DEFAULT_COLORS);

                switch (preset) {
                    case CAMPFIRE -> {
                        if (BLOCKS.sparks.campfireSparks) {
                            ParticleSpawnUtil.spawnSparks(level, random, pos, SparkType.LONG_LIFE,
                                    new Box(0.3, 0.4, 0.3, 0.6, 0.4, 0.6),
                                    new Vec3(0.03, 0.05, 0.03), 10, colors
                            );
                        }
                    }
                    case TORCH -> {
                        if (BLOCKS.sparks.torchSparks) {
                            ParticleSpawnUtil.spawnSparks(level, random, pos, SparkType.SHORT_LIFE,
                                    new Box(0.3, 0.5, 0.3, 0.6, 0.5, 0.6),
                                    new Vec3(0.01, 0.01, 0.01), 2, colors
                            );
                        }
                    }
                    case WALL_TORCH -> {
                        if (!BLOCKS.sparks.torchSparks || !state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                            return;
                        }

                        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
                        Vec3 offset = new Vec3(0.5 + (0.27 * direction.getStepX()), 0.7, 0.5 + (0.27 * direction.getStepZ()));
                        ParticleSpawnUtil.spawnSparks(level, random, pos, SparkType.SHORT_LIFE,
                                new Box(
                                        offset.subtract(0.05, 0, 0.05),
                                        offset.add(0.05, 0, 0.05)
                                ),
                                new Vec3(0.01, 0.01, 0.01), 2, colors
                        );
                    }
                    case CANDLE -> {
                        if (BLOCKS.sparks.candleSparks && block instanceof AbstractCandleBlockAccessor candleBlock) {
                            candleBlock.subtle_effects$getParticleOffsets(state).forEach(offset -> {
                                ParticleSpawnUtil.spawnSparks(level, random, pos, SparkType.SHORT_LIFE, new Box(
                                        offset.subtract(0.05, 0, 0.05),
                                        offset.add(0.05, 0, 0.05)
                                ), new Vec3(0.01, 0.01, 0.01), 2, colors);
                            });
                        }
                    }
                    case LANTERN -> {
                        for (int i = 0; i < BLOCKS.sparks.lanternSparksDensity.get(); i++) {
                            int xSign = nextSign(random);
                            int zSign = nextSign(random);
                            level.addParticle(SparkParticle.create(SparkType.FLOATING, random, colors),
                                    pos.getX() + 0.5 + (nextDouble(random, 0.5) * xSign),
                                    pos.getY() + random.nextInt(5) / 10D,
                                    pos.getZ() + 0.5 + (nextDouble(random, 0.5) * zSign),
                                    nextDouble(random, 0.03) * xSign,
                                    0,
                                    nextDouble(random, 0.03) * zSign
                            );
                        }
                    }
                    case FIRE -> {
                        if (BLOCKS.sparks.fireSparks && block instanceof BaseFireBlockAccessor fireBlock) {
                            BlockPos belowPos = pos.below();
                            BlockState belowState = level.getBlockState(belowPos);

                            if (!fireBlock.subtle_effects$canBurn(belowState) && !belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
                                for (Direction direction : Direction.Plane.HORIZONTAL) {
                                    if (fireBlock.subtle_effects$canBurn(level.getBlockState(pos.relative(direction)))) {
                                        Direction.Axis axis = direction.getAxis();
                                        int i = direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 0 : 1;

                                        spawnFireSparks(level, random, pos, colors,
                                                axis == Direction.Axis.X ? i : random.nextDouble(),
                                                axis == Direction.Axis.Z ? random.nextDouble() : i
                                        );
                                    }
                                }
                                return;
                            }
                            spawnFireSparks(level, random, pos, colors, random.nextDouble(), random.nextDouble());
                        }
                    }
                    case FURNACE -> {
                        if (!BLOCKS.sparks.furnaceSparks || !state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                            return;
                        }

                        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                        Direction.Axis axis = direction.getAxis();

                        for (int i = 0; i < 3; i++) {
                            level.addParticle(SparkParticle.create(SparkType.SHORT_LIFE, random, colors),
                                    pos.getX() + 0.5 + (0.6 * direction.getStepX()) + random.nextDouble()
                                            / (axis == Direction.Axis.X ? 10 : 3) * nextSign(random),
                                    pos.getY() + (random.nextDouble() * 6 / 16),
                                    pos.getZ() + 0.5 + (0.6 * direction.getStepZ()) + random.nextDouble()
                                            / (axis == Direction.Axis.Z ? 10 : 3) * nextSign(random),
                                    nextNonAbsDouble(random, 0.01),
                                    nextNonAbsDouble(random, 0.01),
                                    nextNonAbsDouble(random, 0.01)
                            );
                        }
                    }
                    case CUSTOM -> {
                        Box box = options.box().orElse(new Box());
                        SparkType sparkType = options.sparkType().orElse(SparkType.SHORT_LIFE);
                        Vec3 velocity = options.velocity().orElse(new Vec3(0.03, 0.05, 0.03));
                        IntProvider count = options.count().orElse(ConstantInt.of(10));
                        float chance = options.chance().orElse(1F);

                        if (random.nextFloat() <= chance) {
                            ParticleSpawnUtil.spawnSparks(level, random, pos, sparkType, box, velocity, count.sample(random), colors);
                        }
                    }
                }
            });
        }
    }

    private static void spawnFireSparks(Level level, RandomSource random, BlockPos pos, List<Integer> colors, double xOffset, double zOffset) {
        for (int i = 0; i < 5; i++) {
            level.addParticle(SparkParticle.create(SparkType.LONG_LIFE, random, colors),
                    pos.getX() + xOffset + random.nextDouble() / 10 * nextSign(random),
                    pos.getY() + random.nextDouble(),
                    pos.getZ() + zOffset + random.nextDouble() / 10 * nextSign(random),
                    nextNonAbsDouble(random, 0.03),
                    nextNonAbsDouble(random, 0.05),
                    nextNonAbsDouble(random, 0.03)
            );
        }
    }
}
