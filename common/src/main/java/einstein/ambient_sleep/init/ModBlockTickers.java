package einstein.ambient_sleep.init;

import einstein.ambient_sleep.particle.option.PositionParticleOptions;
import einstein.ambient_sleep.util.ParticleManager;
import einstein.ambient_sleep.util.ParticleSpawnUtil;
import einstein.ambient_sleep.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static einstein.ambient_sleep.init.ModConfigs.INSTANCE;
import static einstein.ambient_sleep.util.MathUtil.*;

public class ModBlockTickers {

    public static final Map<Predicate<BlockState>, ParticleManager.BlockProvider> REGISTERED = new HashMap<>();

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

    private static void register(Block block, ParticleManager.BlockProvider provider) {
        register(state -> state.is(block), provider);
    }

    private static void register(Predicate<BlockState> predicate, ParticleManager.BlockProvider provider) {
        REGISTERED.put(predicate, provider);
    }
}
