package einstein.subtle_effects.init;

import einstein.subtle_effects.particle.option.PositionParticleOptions;
import einstein.subtle_effects.util.BlockProvider;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static einstein.subtle_effects.init.ModConfigs.INSTANCE;
import static einstein.subtle_effects.util.MathUtil.*;

public class ModBlockTickers {

    public static final Map<Predicate<BlockState>, BlockProvider> REGISTERED = new HashMap<>();

    public static void init() {
        register(Blocks.REDSTONE_BLOCK, (state, level, pos, random) -> {
            if (INSTANCE.redstoneBlockDust.get()) {
                ParticleSpawnUtil.spawnParticlesAroundBlock(DustParticleOptions.REDSTONE, level, pos, random);
            }
        });
        register(Blocks.GLOWSTONE, (state, level, pos, random) -> {
            if (INSTANCE.glowstoneBlockDust.get().equals(ModConfigs.GlowstoneDustSpawnType.OFF)
                    || (INSTANCE.glowstoneBlockDust.get().equals(ModConfigs.GlowstoneDustSpawnType.NETHER_ONLY)
                    && !level.dimension().equals(Level.NETHER))) {
                return;
            }

            ParticleSpawnUtil.spawnParticlesAroundBlock(Util.GLOWSTONE_DUST_PARTICLES, level, pos, random);
        });
        register(Blocks.TORCHFLOWER, (state, level, pos, random) -> {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.8;
            double z = pos.getZ() + 0.5;

            if (INSTANCE.torchflowerSmoke.get() != ModConfigs.SmokeType.OFF && random.nextInt(3) == 0) {
                level.addParticle(INSTANCE.torchflowerSmoke.get().getParticle().get(), x, y, z, 0, 0, 0);
            }

            if (INSTANCE.torchflowerFlames.get() && random.nextInt(5) == 0) {
                level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
            }
        });
        register(Blocks.DRAGON_EGG, (state, level, pos, random) -> {
            if (INSTANCE.dragonEggParticles.get()) {
                for (int i = 0; i < 3; ++i) {
                    level.addParticle(ParticleTypes.PORTAL,
                            pos.getX() + 0.5 + 0.25 * nextSign(),
                            pos.getY() + random.nextDouble(),
                            pos.getZ() + 0.5 + 0.25 * nextSign(),
                            nextNonAbsDouble(),
                            (random.nextDouble() - 0.5) * 0.125,
                            nextNonAbsDouble()
                    );
                }
            }
        });
        register(Blocks.LAVA_CAULDRON, (state, level, pos, random) -> {
            if (INSTANCE.lavaCauldronSparks.get()) {
                ParticleSpawnUtil.spawnLavaSparks(level, pos.above(), random, 5);
            }
        });
        register(Blocks.BEACON, (state, level, pos, random) -> {
            if (INSTANCE.beaconParticles.get()) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity) {
                    if (!beaconBlockEntity.getBeamSections().isEmpty()) {
                        PositionParticleOptions options = new PositionParticleOptions(ModParticles.BEACON.get(), beaconBlockEntity.getBlockPos());
                        for (int i = 0; i < 10; i++) {
                            level.addParticle(options,
                                    pos.getX() + 0.5 + nextFloat(30) * nextSign(),
                                    pos.getY() + 1,
                                    pos.getZ() + 0.5 + nextFloat(30) * nextSign(),
                                    0, 0, 0
                            );
                        }
                    }
                }
            }
        });
        register(Blocks.RESPAWN_ANCHOR, (state, level, pos, random) -> {
            if (INSTANCE.respawnAnchorParticles.get()) {
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
            if (ModConfigs.INSTANCE.furnaceSparks.get() && state.getValue(FurnaceBlock.LIT)) {
                Direction direction = state.getValue(FurnaceBlock.FACING);
                Direction.Axis axis = direction.getAxis();
                ParticleSpawnUtil.spawnSparks(level, random, pos, new Vec3(0.5 + (0.6 * direction.getStepX()), random.nextDouble() * 6 / 16, 0.5 + (0.6 * direction.getStepZ())), new Vec3i(1, 1, 1), 3, axis == Direction.Axis.X ? 10 : 3, axis == Direction.Axis.Z ? 10 : 3, false, false);
            }
        });
        register(Blocks.WATER_CAULDRON, (state, level, pos, random) -> {
            ParticleSpawnUtil.spawnHeatedWaterParticles(level, pos, random, false,
                    0.5625 + (state.getValue(LayeredCauldronBlock.LEVEL) * 0.1875),
                    INSTANCE.steamingWaterCauldron, INSTANCE.boilingWaterCauldron
            );
        });
        register(Blocks.END_GATEWAY, (state, level, pos, random) -> {
            if (INSTANCE.endPortalParticles.get()) {
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

        register(state -> state.getBlock() instanceof CampfireBlock && INSTANCE.campfireSparks.get() && state.getValue(CampfireBlock.LIT), (state, level, pos, random) -> {
            ParticleSpawnUtil.spawnSparks(level, random, pos, new Vec3(0.5, 0.4, 0.5),
                    new Vec3i(3, 5, 3), 10, 6, state.is(Blocks.SOUL_CAMPFIRE), true
            );
        });
        register(state -> state.getBlock() instanceof TorchBlock && INSTANCE.torchSparks.get(), (state, level, pos, random) -> {
            ParticleSpawnUtil.spawnSparks(level, random, pos, new Vec3(0.5, 0.5, 0.5),
                    new Vec3i(1, 1, 1), 2, -6, state.is(Blocks.SOUL_TORCH), false
            );
        });
        register(state -> state.getBlock() instanceof WallTorchBlock && INSTANCE.torchSparks.get(), (state, level, pos, random) -> {
            Direction direction = state.getValue(WallTorchBlock.FACING).getOpposite();
            ParticleSpawnUtil.spawnSparks(level, random, pos,
                    new Vec3(0.5 + (0.27 * direction.getStepX()), 0.7, 0.5 + (0.27 * direction.getStepZ())),
                    new Vec3i(1, 1, 1), 2, 20, state.is(Blocks.SOUL_WALL_TORCH), false
            );
        });
        register(state -> state.getBlock() instanceof AbstractCandleBlock && ModConfigs.INSTANCE.candleSparks.get() && state.getValue(AbstractCandleBlock.LIT), (state, level, pos, random) -> {
            AbstractCandleBlock block = (AbstractCandleBlock) state.getBlock();
            block.getParticleOffsets(state).forEach(offset -> ParticleSpawnUtil.spawnSparks(level, random, pos,
                    offset, new Vec3i(1, 1, 1), 1, 20, false, false)
            );
        });
        register(state -> state.getBlock() instanceof BaseFireBlock && ModConfigs.INSTANCE.fireSparks.get(), (state, level, pos, random) -> {
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
        register(state -> state.getBlock() instanceof LanternBlock && INSTANCE.lanternSparks.get(), (state, level, pos, random) -> {
            for (int i = 0; i < 5; i++) {
                int xSign = nextSign();
                int zSign = nextSign();
                level.addParticle(state.is(Blocks.SOUL_LANTERN) ? ModParticles.FLOATING_SOUL_SPARK.get() : ModParticles.FLOATING_SPARK.get(),
                        pos.getX() + 0.5 + random.nextDouble() / 2 * xSign,
                        pos.getY() + random.nextInt(5) / 10D,
                        pos.getZ() + 0.5 + random.nextDouble() / 2 * zSign,
                        random.nextInt(3) / 100D * xSign,
                        0,
                        random.nextInt(3) / 100D * zSign
                );
            }
        });
        register(state -> state.getBlock() instanceof CommandBlock
                && (INSTANCE.commandBlockParticles.get() == ModConfigs.CommandBlockSpawnType.ON
                || (INSTANCE.commandBlockParticles.get() == ModConfigs.CommandBlockSpawnType.NOT_CREATIVE
                && !Minecraft.getInstance().player.isCreative())), (state, level, pos, random) ->
                ParticleSpawnUtil.spawnCmdBlockParticles(level, Vec3.atCenterOf(pos), random, (direction, relativePos) ->
                        !Util.isSolidOrNotEmpty(level, BlockPos.containing(relativePos))
                ));
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

    private static void spawnFireSparks(Level level, RandomSource random, BlockState state, BlockPos pos, double xOffset, double zOffset) {
        ParticleSpawnUtil.spawnSparks(level, random, pos, new Vec3(xOffset, random.nextDouble(), zOffset), new Vec3i(3, 5, 3), 10, 10, state.is(Blocks.SOUL_FIRE), true);
    }
}
