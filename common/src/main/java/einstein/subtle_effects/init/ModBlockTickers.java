package einstein.subtle_effects.init;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.configs.CommandBlockSpawnType;
import einstein.subtle_effects.configs.ModBlockConfigs;
import einstein.subtle_effects.mixin.client.block.AmethystClusterBlockAccessor;
import einstein.subtle_effects.particle.option.PositionParticleOptions;
import einstein.subtle_effects.tickers.FlameGeyserTicker;
import einstein.subtle_effects.tickers.TickerManager;
import einstein.subtle_effects.util.BlockTickerProvider;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.LavaFluid;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;
import static einstein.subtle_effects.util.MathUtil.nextSign;
import static einstein.subtle_effects.util.Util.playClientSound;
import static net.minecraft.util.Mth.nextFloat;

public class ModBlockTickers {

    public static final Map<Block, BlockTickerProvider> REGISTERED = new HashMap<>();
    public static final Map<Predicate<BlockState>, BlockTickerProvider> REGISTERED_SPECIAL = new HashMap<>();

    public static void init() {
        REGISTERED.clear();
        REGISTERED_SPECIAL.clear();

        register(Blocks.REDSTONE_BLOCK, () -> BLOCKS.redstoneBlockDust, (state, level, pos, random) -> {
            ParticleSpawnUtil.spawnParticlesAroundBlock(DustParticleOptions.REDSTONE, level, pos, random, BLOCKS.redstoneBlockDustDensity.getPerSideChance());
        });
        register(Blocks.GLOWSTONE, () -> BLOCKS.glowstoneBlockDustDisplayType != ModBlockConfigs.GlowstoneDustDisplayType.OFF,
                (state, level, pos, random) -> {
                    if (BLOCKS.glowstoneBlockDustDisplayType.equals(ModBlockConfigs.GlowstoneDustDisplayType.NETHER_ONLY)
                            && !level.dimension().equals(Level.NETHER)) {
                        return;
                    }

                    ParticleSpawnUtil.spawnParticlesAroundBlock(Util.GLOWSTONE_DUST_PARTICLES, level, pos, random, BLOCKS.glowstoneBlockDustDensity.getPerSideChance());
                });
        register(Blocks.TORCHFLOWER, () -> BLOCKS.torchflowerSmoke.isEnabled() || BLOCKS.torchflowerFlames,
                (state, level, pos, random) -> {
                    Vec3 center = state.getShape(level, pos).bounds().getCenter();
                    Vec3 offsetPos = new Vec3(
                            pos.getX() + center.x(),
                            pos.getY() + center.y() + 0.3,
                            pos.getZ() + center.z()
                    );

                    if (BLOCKS.torchflowerSmoke.isEnabled() && random.nextInt(3) == 0) {
                        level.addParticle(BLOCKS.torchflowerSmoke.getParticle().get(),
                                offsetPos.x(),
                                offsetPos.y(),
                                offsetPos.z(),
                                0, 0, 0
                        );
                    }

                    if (BLOCKS.torchflowerFlames && random.nextInt(5) == 0) {
                        level.addParticle(ParticleTypes.FLAME,
                                offsetPos.x(),
                                offsetPos.y(),
                                offsetPos.z(),
                                0, 0, 0
                        );
                    }
                });
        register(Blocks.DRAGON_EGG, () -> BLOCKS.dragonEggParticles, (state, level, pos, random) -> {
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
        });
        register(Blocks.LAVA_CAULDRON, () -> BLOCKS.lavaCauldronEffects, (state, level, pos, random) -> {
            ((LavaFluid.Source) Fluids.LAVA).animateTick(level, pos, Fluids.LAVA.defaultFluidState().setValue(BlockStateProperties.FALLING, false), random);
        });
        register(Blocks.BEACON, () -> BLOCKS.beaconParticlesDisplayType != ModBlockConfigs.BeaconParticlesDisplayType.OFF,
                (state, level, pos, random) -> {
                    BlockEntity blockEntity = level.getBlockEntity(pos);

                    if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity) {
                        List<BeaconBlockEntity.BeaconBeamSection> sections = beaconBlockEntity.getBeamSections();

                        if (!sections.isEmpty() && !(sections.size() > 1 && BLOCKS.beaconParticlesDisplayType == ModBlockConfigs.BeaconParticlesDisplayType.NOT_COLORED)) {
                            PositionParticleOptions options = new PositionParticleOptions(ModParticles.BEACON.get(), beaconBlockEntity.getBlockPos());

                            for (int i = 0; i < BLOCKS.beaconParticlesDensity.get(); i++) {
                                level.addParticle(options,
                                        pos.getX() + 0.5 + nextNonAbsDouble(random, 0.3),
                                        pos.getY() + 0.5,
                                        pos.getZ() + 0.5 + nextNonAbsDouble(random, 0.3),
                                        0, 0, 0
                                );
                            }
                        }
                    }
                });
        register(Blocks.RESPAWN_ANCHOR, () -> BLOCKS.respawnAnchorParticles, (state, level, pos, random) -> {
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
        });
        register(Blocks.WATER_CAULDRON, () -> BLOCKS.steam.steamingWaterCauldron || BLOCKS.steam.boilingWaterCauldron,
                (state, level, pos, random) -> {
                    ParticleSpawnUtil.spawnHeatedWaterParticles(level, pos, random, false,
                            0.5625 + (state.getValue(LayeredCauldronBlock.LEVEL) * 0.1875),
                            BLOCKS.steam.steamingWaterCauldron, BLOCKS.steam.boilingWaterCauldron
                    );
                });
        register(Blocks.END_GATEWAY, () -> BLOCKS.endPortalParticles, (state, level, pos, random) -> {
            for (int i = 0; i < 5; i++) {
                level.addParticle(ModParticles.END_PORTAL.get(),
                        pos.getX() + (random.nextInt(3) - 1) + random.nextDouble(),
                        pos.getY() + (random.nextInt(3) - 1) + random.nextDouble(),
                        pos.getZ() + (random.nextInt(3) - 1) + random.nextDouble(),
                        0, 0, 0
                );
            }
        });
        register(state -> state.getBlock() instanceof CampfireBlock && state.getValue(CampfireBlock.LIT),
                () -> BLOCKS.campfireSizzlingSoundVolume.get() > 0, (state, level, pos, random) -> {
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
                });
        register(state -> state.getBlock() instanceof CommandBlock, () -> BLOCKS.commandBlockParticles != CommandBlockSpawnType.OFF,
                (state, level, pos, random) -> {
                    if (BLOCKS.commandBlockParticles == CommandBlockSpawnType.NOT_CREATIVE && Minecraft.getInstance().player.isCreative()) {
                        return;
                    }

                    ParticleSpawnUtil.spawnCmdBlockParticles(level, Vec3.atCenterOf(pos), random, (direction, relativePos) ->
                            !Util.isSolidOrNotEmpty(level, BlockPos.containing(relativePos)));
                });
        register(state -> state.is(Blocks.AMETHYST_BLOCK) || state.is(Blocks.BUDDING_AMETHYST),
                () -> BLOCKS.amethystSparkleDisplayType == ModBlockConfigs.AmethystSparkleDisplayType.ON,
                (state, level, pos, random) -> {
                    ParticleSpawnUtil.spawnParticlesAroundBlock(ModParticles.AMETHYST_SPARKLE.get(), level, pos, random, 5);
                });
        register(state -> state.getBlock() instanceof AmethystClusterBlock, () -> BLOCKS.amethystSparkleSounds,
                (state, level, pos, random) -> {
                    if (((AmethystClusterBlockAccessor) state.getBlock()).getHeight() >= 5) {
                        int chance = random.nextInt(100);
                        if (chance <= 5) {
                            if (chance == 0) {
                                playClientSound(pos, ModSounds.AMETHYST_CLUSTER_CHIME.get(), SoundSource.BLOCKS, nextFloat(random, 0.15F, 0.3F), 1);
                                return;
                            }
                            playClientSound(pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, nextFloat(random, 0.07F, 1.5F), nextFloat(random, 0.07F, 1.3F));
                        }
                    }
                });
        register(state -> state.is(Blocks.LARGE_AMETHYST_BUD) || state.is(Blocks.AMETHYST_CLUSTER),
                () -> BLOCKS.amethystSparkleDisplayType != ModBlockConfigs.AmethystSparkleDisplayType.OFF,
                (state, level, pos, random) -> {
                    AmethystClusterBlockAccessor block = (AmethystClusterBlockAccessor) state.getBlock();
                    float height = block.getHeight();

                    if (height >= 5) {
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
                });
        register(Blocks.FLOWERING_AZALEA_LEAVES, () -> BLOCKS.floweringAzaleaPetals, (state, level, pos, random) -> {
            if (random.nextInt(10) == 0) {
                BlockPos belowPos = pos.below();
                BlockState belowState = level.getBlockState(belowPos);

                if (!Block.isFaceFull(belowState.getCollisionShape(level, belowPos), Direction.UP)) {
                    ParticleUtils.spawnParticleBelow(level, pos, random, ModParticles.AZALEA_PETAL.get());
                }
            }
        });
        register(Blocks.SCULK, () -> BLOCKS.sculkBlockSculkDust, (state, level, pos, random) -> {
            if (random.nextInt(20) == 0) {
                ParticleSpawnUtil.spawnParticlesAroundBlock(ModParticles.SCULK_DUST.get(), level, pos, random, 0);
            }
        });
        register(Blocks.SCULK_VEIN, () -> BLOCKS.sculkVeinSculkDust, (state, level, pos, random) -> {
            if (random.nextInt(30) == 0) {
                ParticleSpawnUtil.spawnParticlesAroundBlock(ModParticles.SCULK_DUST.get(), level, pos, random, -0.9375F,
                        direction -> direction.getAxis() != Direction.Axis.Y);
            }
        });
        register(Blocks.CALIBRATED_SCULK_SENSOR, () -> BLOCKS.calibratedSculkSensorAmethystSparkle, (state, level, pos, random) -> {
            if (random.nextInt(SculkSensorBlock.getPhase(state) == SculkSensorPhase.ACTIVE ? 1 : 5) == 0) {
                level.addParticle(ModParticles.AMETHYST_SPARKLE.get(),
                        pos.getX() + 0.5 + nextNonAbsDouble(random, 0.25),
                        pos.getY() + 0.5 + nextNonAbsDouble(random, 0.75),
                        pos.getZ() + 0.5 + nextNonAbsDouble(random, 0.25),
                        0, 0, 0
                );
            }
        });
        register(state -> (state.is(BlockTags.FLOWERS) || (BLOCKS.vegetationFirefliesSpawnType == ModBlockConfigs.VegetationFirefliesSpawnType.GRASS_AND_FLOWERS && (state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS)))),
                () -> BLOCKS.vegetationFirefliesDensity.get() > 0,
                (state, level, pos, random) -> {
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
        // TODO should also spawn from either blackstone or basalt so it can be in basalt deltas (probably blackstone)
        register(Blocks.NETHERRACK, () -> true, (state, level, pos, random) -> {
//            if (level.dimension().equals(Level.NETHER)) {
            BlockPos abovePos = pos.above();
            // TODO doesn't work right, ends up spawning them in square clusters
//            if (Double.parseDouble("0." + Math.abs(pos.hashCode())) < 0.2) { // also too common
            RandomSource blockRandom = RandomSource.create(state.getSeed(pos));
            if (blockRandom.nextDouble() < 0.005) {
                if (level.getBlockState(pos).is(Blocks.NETHERRACK)) {
                    if (!FlameGeyserTicker.ACTIVE_GEYSERS.contains(pos) && !FlameGeyserTicker.INACTIVE_GEYSERS.contains(pos)) {
                        // TODO shouldn't be able to spawn with fire above
                        //  also needs to check every block in geyser path
                        //  update ticker with changes
                        if (!level.getBlockState(abovePos).isFaceSturdy(level, pos, Direction.DOWN) && level.getFluidState(abovePos).isEmpty()) {
                            TickerManager.add(new FlameGeyserTicker(level, pos, blockRandom));
                        }
                    }
                }
            }
//            }
        });
    }

    private static void register(Block block, Supplier<Boolean> isEnabled, BlockTickerProvider provider) {
        if (isEnabled.get()) {
            if (REGISTERED.put(block, provider) != null) {
                SubtleEffects.LOGGER.error("Found duplicate block tickers using {}", BuiltInRegistries.BLOCK.getKey(block));
            }
        }
    }

    private static void register(Predicate<BlockState> predicate, Supplier<Boolean> isEnabled, BlockTickerProvider provider) {
        if (isEnabled.get()) {
            REGISTERED_SPECIAL.put(predicate, provider);
        }
    }
}
