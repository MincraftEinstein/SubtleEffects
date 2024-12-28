package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.data.SparkProvider;
import einstein.subtle_effects.data.SparkProviderReloadListener;
import einstein.subtle_effects.init.ModBlockTickers;
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.tickers.TickerManager;
import einstein.subtle_effects.util.Box;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.SparkType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static einstein.subtle_effects.util.MathUtil.nextDouble;
import static einstein.subtle_effects.util.MathUtil.nextSign;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {

    protected ClientLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionType, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionType, profiler, isClientSide, isDebug, biomeZoomSeed, maxNeighborUpdates);
    }

    @Inject(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void entityTick(Entity entity, CallbackInfo ci) {
        TickerManager.createTickersForEntity(entity);
    }

    @Inject(method = "tickPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;rideTick()V"))
    private void entityRideTick(Entity vehicleEntity, Entity entity, CallbackInfo ci) {
        TickerManager.createTickersForEntity(entity);
    }

    @Inject(method = "doAnimateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"))
    private void animateTick(int x, int y, int z, int range, RandomSource random, Block markerBlock, BlockPos.MutableBlockPos pos, CallbackInfo ci) {
        BlockState state = getBlockState(pos);
        Block block = state.getBlock();

        if (!state.isAir()) {
            ModBlockTickers.REGISTERED.forEach((predicate, provider) -> {
                if (predicate.test(state)) {
                    provider.apply(state, this, pos, random);
                }
            });

            SparkProviderReloadListener.SPARK_PROVIDERS.forEach((id, provider) -> {
                provider.options().ifPresent(options -> {
                    SparkProvider.PresetType preset = options.preset();
                    List<Integer> colors = options.colors().orElse(SparkParticle.DEFAULT_COLORS);

                    provider.states().forEach(stateHolder -> {
                        if (stateHolder.matches(state)) {
                            switch (preset) {
                                case CAMPFIRE -> {
                                    if (BLOCKS.sparks.campfireSparks) {
                                        ParticleSpawnUtil.spawnSparks(this, random, pos, SparkType.LONG_LIFE,
                                                new Box(0.3, 0.4, 0.3, 0.6, 0.4, 0.6),
                                                new Vec3(0.03, 0.05, 0.03), 10, colors
                                        );
                                    }
                                }
                                case TORCH -> {
                                    if (BLOCKS.sparks.torchSparks) {
                                        ParticleSpawnUtil.spawnSparks(this, random, pos, SparkType.SHORT_LIFE,
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
                                    ParticleSpawnUtil.spawnSparks(this, random, pos, SparkType.SHORT_LIFE,
                                            new Box(
                                                    offset.subtract(0.05, 0, 0.05),
                                                    offset.add(0.05, 0, 0.05)
                                            ),
                                            new Vec3(0.01, 0.01, 0.01), 2, colors
                                    );
                                }
                                case CANDLE -> {
                                    if (BLOCKS.sparks.candleSparks && block instanceof AbstractCandleBlock candleBlock) {
                                        candleBlock.getParticleOffsets(state).forEach(offset -> {
                                            ParticleSpawnUtil.spawnSparks(this, random, pos, SparkType.SHORT_LIFE, new Box(
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
                                        addParticle(SparkParticle.create(SparkType.FLOATING, random, colors),
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
                                    if (BLOCKS.sparks.fireSparks && block instanceof BaseFireBlock fireBlock) {
                                        BlockPos belowPos = pos.below();
                                        BlockState belowState = getBlockState(belowPos);

                                        if (!fireBlock.canBurn(belowState) && !belowState.isFaceSturdy(this, belowPos, Direction.UP)) {
                                            for (Direction direction : Direction.Plane.HORIZONTAL) {
                                                if (fireBlock.canBurn(getBlockState(pos.relative(direction)))) {
                                                    Direction.Axis axis = direction.getAxis();
                                                    int i = direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 0 : 1;

                                                    subtleEffects$spawnFireSparks(this, random, pos, colors,
                                                            axis == Direction.Axis.X ? i : random.nextDouble(),
                                                            axis == Direction.Axis.Z ? random.nextDouble() : i
                                                    );
                                                }
                                            }
                                            return;
                                        }
                                        subtleEffects$spawnFireSparks(this, random, pos, colors, random.nextDouble(), random.nextDouble());
                                    }
                                }
                                case FURNACE -> {
                                    if (!BLOCKS.sparks.furnaceSparks || !state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                                        return;
                                    }

                                    Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                                    Direction.Axis axis = direction.getAxis();

                                    for (int i = 0; i < 3; i++) {
                                        addParticle(SparkParticle.create(SparkType.SHORT_LIFE, random, colors),
                                                pos.getX() + 0.5 + (0.6 * direction.getStepX()) + random.nextDouble()
                                                        / (axis == Direction.Axis.X ? 10 : 3) * nextSign(random),
                                                pos.getY() + (random.nextDouble() * 6 / 16),
                                                pos.getZ() + 0.5 + (0.6 * direction.getStepZ()) + random.nextDouble()
                                                        / (axis == Direction.Axis.Z ? 10 : 3) * nextSign(random),
                                                0.01, 0.01, 0.01
                                        );
                                    }
                                }
                                case CUSTOM -> {
                                    Box box = options.box().orElse(new Box());
                                    SparkType sparkType = options.sparkType().orElse(SparkType.SHORT_LIFE);
                                    Vec3 velocity = options.velocity().orElse(new Vec3(0.03, 0.05, 0.03));
                                    int count = options.count().orElse(10);

                                    ParticleSpawnUtil.spawnSparks(this, random, pos, sparkType, box, velocity, count, colors);
                                }
                            }
                        }
                    });
                });
            });
        }
    }

    @Unique
    private static void subtleEffects$spawnFireSparks(Level level, RandomSource random, BlockPos pos, List<Integer> colors, double xOffset, double zOffset) {
        for (int i = 0; i < 5; i++) {
            level.addParticle(SparkParticle.create(SparkType.LONG_LIFE, random, colors),
                    pos.getX() + xOffset + random.nextDouble() / 10 * nextSign(random),
                    pos.getY() + random.nextDouble(),
                    pos.getZ() + zOffset + random.nextDouble() / 10 * nextSign(random),
                    0.03, 0.05, 0.03
            );
        }
    }
}
