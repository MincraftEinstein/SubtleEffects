package einstein.subtle_effects.init;

import einstein.subtle_effects.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.configs.ModBlockConfigs;
import einstein.subtle_effects.configs.SmokeType;
import einstein.subtle_effects.mixin.client.block.AmethystClusterBlockAccessor;
import einstein.subtle_effects.particle.option.PositionParticleOptions;
import einstein.subtle_effects.util.BlockProvider;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;
import static einstein.subtle_effects.util.MathUtil.nextSign;
import static einstein.subtle_effects.util.Util.playClientSound;
import static net.minecraft.util.Mth.nextFloat;

public class ModBlockTickers {

    public static final Map<Predicate<BlockState>, BlockProvider> REGISTERED = new HashMap<>();

    public static void init() {
        register(Blocks.REDSTONE_BLOCK, (state, level, pos, random) -> {
            if (BLOCKS.redstoneBlockDust) {
                ParticleSpawnUtil.spawnParticlesAroundBlock(DustParticleOptions.REDSTONE, level, pos, random, BLOCKS.redstoneBlockDustDensity.getPerSideChance());
            }
        });
        register(Blocks.GLOWSTONE, (state, level, pos, random) -> {
            if (BLOCKS.glowstoneBlockDustDisplayType.equals(ModBlockConfigs.GlowstoneDustDisplayType.OFF)
                    || (BLOCKS.glowstoneBlockDustDisplayType.equals(ModBlockConfigs.GlowstoneDustDisplayType.NETHER_ONLY)
                    && !level.dimension().equals(Level.NETHER))) {
                return;
            }

            ParticleSpawnUtil.spawnParticlesAroundBlock(Util.GLOWSTONE_DUST_PARTICLES, level, pos, random, BLOCKS.glowstoneBlockDustDensity.getPerSideChance());
        });
        register(Blocks.TORCHFLOWER, (state, level, pos, random) -> {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.8;
            double z = pos.getZ() + 0.5;

            if (BLOCKS.torchflowerSmoke != SmokeType.OFF && random.nextInt(3) == 0) {
                level.addParticle(BLOCKS.torchflowerSmoke.getParticle().get(), x, y, z, 0, 0, 0);
            }

            if (BLOCKS.torchflowerFlames && random.nextInt(5) == 0) {
                level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
            }
        });
        register(Blocks.DRAGON_EGG, (state, level, pos, random) -> {
            if (BLOCKS.dragonEggParticles) {
                for (int i = 0; i < 3; ++i) {
                    level.addParticle(ParticleTypes.PORTAL,
                            pos.getX() + 0.5 + 0.25 * nextSign(random),
                            pos.getY() + random.nextDouble(),
                            pos.getZ() + 0.5 + 0.25 * nextSign(random),
                            nextNonAbsDouble(random),
                            (random.nextDouble() - 0.5) * 0.125,
                            nextNonAbsDouble(random)
                    );
                }
            }
        });
        register(Blocks.LAVA_CAULDRON, (state, level, pos, random) -> {
            if (BLOCKS.sparks.lavaCauldronSparks) {
                ParticleSpawnUtil.spawnLavaSparks(level, pos.above(), random, 5);
            }
        });
        register(Blocks.BEACON, (state, level, pos, random) -> {
            if (BLOCKS.beaconParticles) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity) {
                    if (!beaconBlockEntity.getBeamSections().isEmpty()) {
                        PositionParticleOptions options = new PositionParticleOptions(ModParticles.BEACON.get(), beaconBlockEntity.getBlockPos());
                        for (int i = 0; i < 10; i++) {
                            level.addParticle(options,
                                    pos.getX() + 0.5 + nextNonAbsDouble(random, 0.3),
                                    pos.getY() + 1,
                                    pos.getZ() + 0.5 + nextNonAbsDouble(random, 0.3),
                                    0, 0, 0
                            );
                        }
                    }
                }
            }
        });
        register(Blocks.RESPAWN_ANCHOR, (state, level, pos, random) -> {
            if (BLOCKS.respawnAnchorParticles) {
                if (random.nextInt(5) == 0) {
                    Direction direction = Direction.getRandom(random);

                    if (direction != Direction.UP) {
                        BlockPos relativePos = pos.relative(direction);
                        BlockState relativeState = level.getBlockState(relativePos);

                        if (!state.canOcclude() || !relativeState.isFaceSturdy(level, relativePos, direction.getOpposite())) {
                            ParticleSpawnUtil.spawnParticlesOnSide(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, 0.1F, direction, level, pos, random, 0, 0, 0);
                        }
                    }
                }
            }
        });
        register(Blocks.FURNACE, (state, level, pos, random) -> {
            if (BLOCKS.sparks.furnaceSparks && state.getValue(FurnaceBlock.LIT)) {
                Direction direction = state.getValue(FurnaceBlock.FACING);
                Direction.Axis axis = direction.getAxis();
                ParticleSpawnUtil.spawnSparks(level, random, pos,
                        new Vec3(
                                0.5 + (0.6 * direction.getStepX()),
                                random.nextDouble() * 6 / 16,
                                0.5 + (0.6 * direction.getStepZ())
                        ),
                        new Vec3(0.01, 0.01, 0.01),
                        3,
                        axis == Direction.Axis.X ? 10 : 3,
                        axis == Direction.Axis.Z ? 10 : 3,
                        false, false
                );
            }
        });
        register(Blocks.WATER_CAULDRON, (state, level, pos, random) -> {
            ParticleSpawnUtil.spawnHeatedWaterParticles(level, pos, random, false,
                    0.5625 + (state.getValue(LayeredCauldronBlock.LEVEL) * 0.1875),
                    BLOCKS.steam.steamingWaterCauldron, BLOCKS.steam.boilingWaterCauldron
            );
        });
        register(Blocks.END_GATEWAY, (state, level, pos, random) -> {
            if (BLOCKS.endPortalParticles) {
                for (int i = 0; i < 5; i++) {
                    level.addParticle(ModParticles.END_PORTAL.get(),
                            pos.getX() + (random.nextInt(3) - 1) + random.nextDouble(),
                            pos.getY() + (random.nextInt(3) - 1) + random.nextDouble(),
                            pos.getZ() + (random.nextInt(3) - 1) + random.nextDouble(),
                            0, 0, 0
                    );
                }
            }
        });

        register(state -> state.getBlock() instanceof CampfireBlock
                && state.getValue(CampfireBlock.LIT), (state, level, pos, random) -> {
            if (BLOCKS.sparks.campfireSparks) {
                ParticleSpawnUtil.spawnSparks(level, random, pos, new Vec3(0.5, 0.4, 0.5),
                        new Vec3(0.03, 0.05, 0.03), 10, 6, isSoulFlameBlock(state, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE), true
                );
            }

            if (BLOCKS.campfireSizzlingSounds) {
                if (level.getBlockEntity(pos) instanceof CampfireBlockEntity blockEntity) {
                    for (ItemStack stack : blockEntity.getItems()) {
                        if (!stack.isEmpty() && random.nextInt(5) == 0) {
                            playClientSound(pos, ModSounds.CAMPFIRE_SIZZLE.get(), SoundSource.BLOCKS,
                                    nextFloat(random, 0.3F, 0.7F) * (BLOCKS.campfireSizzlingSoundVolume.get() * 2),
                                    nextFloat(random, 1F, 1.5F)
                            );
                        }
                    }
                }
            }
        });
        register(state -> state.getBlock() instanceof TorchBlock && !(state.getBlock() instanceof WallTorchBlock)
                && BLOCKS.sparks.torchSparks, (state, level, pos, random) -> {
            ParticleSpawnUtil.spawnSparks(level, random, pos, new Vec3(0.5, 0.5, 0.5),
                    new Vec3(0.01, 0.01, 0.01), 2, -6, isSoulFlameBlock(state, Blocks.TORCH, Blocks.SOUL_TORCH), false
            );
        });
        register(state -> state.getBlock() instanceof WallTorchBlock && BLOCKS.sparks.torchSparks,
                (state, level, pos, random) -> {
                    Direction direction = state.getValue(WallTorchBlock.FACING).getOpposite();
                    ParticleSpawnUtil.spawnSparks(level, random, pos,
                            new Vec3(0.5 + (0.27 * direction.getStepX()), 0.7, 0.5 + (0.27 * direction.getStepZ())),
                            new Vec3(0.01, 0.01, 0.01), 2, 20, isSoulFlameBlock(state, Blocks.WALL_TORCH, Blocks.SOUL_WALL_TORCH), false
                    );
                });
        register(state -> state.getBlock() instanceof AbstractCandleBlock && BLOCKS.sparks.candleSparks
                && state.getValue(AbstractCandleBlock.LIT), (state, level, pos, random) -> {
            AbstractCandleBlock block = (AbstractCandleBlock) state.getBlock();
            block.getParticleOffsets(state).forEach(offset -> ParticleSpawnUtil.spawnSparks(level, random, pos,
                    offset, new Vec3(0.01, 0.01, 0.01), 1, 20, isSoulFlameBlock(state, Blocks.CANDLE, null), false)
            );
        });
        register(state -> state.getBlock() instanceof BaseFireBlock && BLOCKS.sparks.fireSparks,
                (state, level, pos, random) -> {
                    BaseFireBlock block = (BaseFireBlock) state.getBlock();
                    BlockPos belowPos = pos.below();
                    BlockState belowState = level.getBlockState(belowPos);

                    if (!block.canBurn(belowState) && !belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
                        if (canBurn(block, level, pos.west())) {
                            spawnFireSparks(level, random, state, pos, 0, random.nextDouble());
                        }

                        if (canBurn(block, level, pos.east())) {
                            spawnFireSparks(level, random, state, pos, 1, random.nextDouble());
                        }

                        if (canBurn(block, level, pos.north())) {
                            spawnFireSparks(level, random, state, pos, random.nextDouble(), 0);
                        }

                        if (canBurn(block, level, pos.south())) {
                            spawnFireSparks(level, random, state, pos, random.nextDouble(), 1);
                        }
                        return;
                    }
                    spawnFireSparks(level, random, state, pos, random.nextDouble(), random.nextDouble());
                });
        register(state -> state.getBlock() instanceof LanternBlock && BLOCKS.sparks.lanternSparksDensity.get() > 0,
                (state, level, pos, random) -> {
                    for (int i = 0; i < BLOCKS.sparks.lanternSparksDensity.get(); i++) {
                        int xSign = nextSign(random);
                        int zSign = nextSign(random);
                        level.addParticle(isSoulFlameBlock(state, Blocks.LANTERN, Blocks.SOUL_LANTERN)
                                        ? ModParticles.FLOATING_SOUL_SPARK.get()
                                        : ModParticles.FLOATING_SPARK.get(),
                                pos.getX() + 0.5 + random.nextDouble() / 2 * xSign,
                                pos.getY() + random.nextInt(5) / 10D,
                                pos.getZ() + 0.5 + random.nextDouble() / 2 * zSign,
                                random.nextInt(3) / 100D * xSign,
                                0,
                                random.nextInt(3) / 100D * zSign
                        );
                    }
                });
        register(state -> state.getBlock() instanceof CommandBlock && BLOCKS.commandBlockParticles.canTick(),
                (state, level, pos, random) ->
                        ParticleSpawnUtil.spawnCmdBlockParticles(level, Vec3.atCenterOf(pos), random, (direction, relativePos) ->
                                !Util.isSolidOrNotEmpty(level, BlockPos.containing(relativePos)))
        );
        register(state -> state.is(Blocks.AMETHYST_BLOCK) || state.is(Blocks.BUDDING_AMETHYST), (state, level, pos, random) -> {
            if (BLOCKS.amethystSparkleDisplayType == ModBlockConfigs.AmethystSparkleDisplayType.ON) {
                ParticleSpawnUtil.spawnParticlesAroundBlock(ModParticles.AMETHYST_SPARKLE.get(), level, pos, random, 5);
            }
        });
        register(state -> state.getBlock() instanceof AmethystClusterBlock, (state, level, pos, random) -> {
            AmethystClusterBlockAccessor block = (AmethystClusterBlockAccessor) state.getBlock();
            float height = block.getHeight();

            if (height >= 5) {
                if (BLOCKS.amethystSparkleDisplayType != ModBlockConfigs.AmethystSparkleDisplayType.OFF) {
                    if (random.nextInt(5) == 0) {
                        float offset = (block.getAABBOffset() / 16) + 0.0625F;
                        float pixelHeight = height / 16;

                        level.addParticle(ModParticles.AMETHYST_SPARKLE.get(),
                                pos.getX() + 0.5 + nextNonAbsDouble(random, offset),
                                pos.getY() + pixelHeight + (pixelHeight / 2),
                                pos.getZ() + 0.5 + nextNonAbsDouble(random, offset),
                                0, 0, 0
                        );
                    }
                }

                if (BLOCKS.amethystSparkleSounds) {
                    int chance = random.nextInt(100);
                    if (chance <= 5) {
                        if (chance == 0) {
                            playClientSound(pos, ModSounds.AMETHYST_CLUSTER_CHIME.get(), SoundSource.BLOCKS, nextFloat(random, 0.15F, 0.3F), 1);
                            return;
                        }
                        playClientSound(pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, nextFloat(random, 0.07F, 1.5F), nextFloat(random, 0.07F, 1.3F));
                    }
                }
            }
        });
        register(Blocks.FLOWERING_AZALEA_LEAVES, (state, level, pos, random) -> {
            if (BLOCKS.floweringAzaleaPetals) {
                if (random.nextInt(10) == 0) {
                    BlockPos belowPos = pos.below();
                    BlockState belowState = level.getBlockState(belowPos);

                    if (!Block.isFaceFull(belowState.getCollisionShape(level, belowPos), Direction.UP)) {
                        ParticleUtils.spawnParticleBelow(level, pos, random, ModParticles.AZALEA_PETAL.get());
                    }
                }
            }
        });
        register(Blocks.SCULK, (state, level, pos, random) -> {
            if (BLOCKS.sculkBlockSculkDust) {
                if (random.nextInt(20) == 0) {
                    ParticleSpawnUtil.spawnParticlesAroundBlock(ModParticles.SCULK_DUST.get(), level, pos, random, 0);
                }
            }
        });
        register(Blocks.SCULK_VEIN, (state, level, pos, random) -> {
            if (BLOCKS.sculkVeinSculkDust) {
                if (random.nextInt(30) == 0) {
                    ParticleSpawnUtil.spawnParticlesAroundBlock(ModParticles.SCULK_DUST.get(), level, pos, random, -0.9375F,
                            direction -> direction.getAxis() != Direction.Axis.Y);
                }
            }
        });
        register(Blocks.CALIBRATED_SCULK_SENSOR, (state, level, pos, random) -> {
            if (BLOCKS.calibratedSculkSensorAmethystSparkle) {
                if (random.nextInt(SculkSensorBlock.getPhase(state) == SculkSensorPhase.ACTIVE ? 1 : 5) == 0) {
                    level.addParticle(ModParticles.AMETHYST_SPARKLE.get(),
                            pos.getX() + 0.5 + nextNonAbsDouble(random, 0.25),
                            pos.getY() + 0.5 + nextNonAbsDouble(random, 0.75),
                            pos.getZ() + 0.5 + nextNonAbsDouble(random, 0.25),
                            0, 0, 0
                    );
                }
            }
        });
        register(state -> (state.is(BlockTags.FLOWERS) || (
                BLOCKS.vegetationFirefliesSpawnType == ModBlockConfigs.VegetationFirefliesSpawnType.GRASS_AND_FLOWERS
                        && (state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS))
        )) && BLOCKS.vegetationFirefliesDensity.get() > 0, (state, level, pos, random) -> {
            if (BiomeParticleManager.FIREFLY_CONDITIONS.test(level, pos)) {
                if (random.nextInt(BLOCKS.vegetationFirefliesDensity.get()) == 0) {
                    level.addParticle(ModParticles.FIREFLY.get(),
                            pos.getX() + nextNonAbsDouble(random),
                            pos.getY() + random.nextDouble(),
                            pos.getZ() + nextNonAbsDouble(random),
                            0, 0, 0
                    );
                }
            }
        });
    }

    private static void register(Block block, BlockProvider provider) {
        register(state -> state.is(block), provider);
    }

    private static void register(Predicate<BlockState> predicate, BlockProvider provider) {
        REGISTERED.put(predicate, provider);
    }

    private static boolean canBurn(BaseFireBlock block, Level level, BlockPos pos) {
        return block.canBurn(level.getBlockState(pos));
    }

    private static boolean isSoulFlameBlock(BlockState state, Block normalBlock, @Nullable Block soulBlock) {
        return !state.is(normalBlock) &&
                ((soulBlock != null && state.is(soulBlock)) ||
                        BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().contains("soul"));
    }

    private static void spawnFireSparks(Level level, RandomSource random, BlockState state, BlockPos pos, double xOffset, double zOffset) {
        ParticleSpawnUtil.spawnSparks(level, random, pos,
                new Vec3(xOffset, random.nextDouble(), zOffset),
                new Vec3(0.03, 0.05, 0.03),
                10, 10,
                isSoulFlameBlock(state, Blocks.FIRE, Blocks.SOUL_FIRE), true
        );
    }
}
