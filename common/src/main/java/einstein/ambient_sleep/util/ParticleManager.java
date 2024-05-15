package einstein.ambient_sleep.util;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.particle.option.PositionParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

import static einstein.ambient_sleep.init.ModConfigs.INSTANCE;
import static einstein.ambient_sleep.util.MathUtil.*;

public class ParticleManager {

    private static final Map<EntityType<?>, EntityProvider<?>> ON_ENTITY_HURT_PROVIDERS = new HashMap<>();
    private static final Map<Block, BlockProvider> BLOCK_ANIMATE_TICK_PROVIDERS = new HashMap<>();

    public static void init() {
        // Entity Hurt
        registerOnEntityHurtProvider(EntityType.CHICKEN, (entity, level, random) -> {
            if (INSTANCE.chickenHitFeathers.get()) {
                for (int i = 0; i < 10; i++) {
                    level.addParticle(ModParticles.CHICKEN_FEATHER.get(),
                            entity.getX(),
                            entity.getY(0.5),
                            entity.getZ(),
                            nextNonAbsDouble(),
                            nextNonAbsDouble(),
                            nextNonAbsDouble()
                    );
                }
            }
        });
        registerOnEntityHurtProvider(EntityType.PARROT, (entity, level, random) -> {
            if (INSTANCE.parrotHitFeathers.get()) {
                ParticleOptions particle = switch (entity.getVariant()) {
                    case BLUE -> ModParticles.BLUE_PARROT_FEATHER.get();
                    case GRAY -> ModParticles.GRAY_PARROT_FEATHER.get();
                    case GREEN -> ModParticles.GREEN_PARROT_FEATHER.get();
                    case RED_BLUE -> ModParticles.RED_BLUE_PARROT_FEATHER.get();
                    case YELLOW_BLUE -> ModParticles.YELLOW_BLUE_PARROT_FEATHER.get();
                };

                for (int i = 0; i < 5; i++) {
                    level.addParticle(particle,
                            entity.getX(),
                            entity.getY(0.5),
                            entity.getZ(),
                            nextNonAbsDouble(),
                            nextNonAbsDouble(),
                            nextNonAbsDouble()
                    );
                }
            }
        });
        registerOnEntityHurtProvider(EntityType.SNOW_GOLEM, (entity, level, random) -> {
            if (INSTANCE.snowGolemHitSnowflakes.get()) {
                for (int i = 0; i < 20; i++) {
                    level.addParticle(ModParticles.SNOW.get(),
                            entity.getX(),
                            entity.getY(random.nextFloat()),
                            entity.getZ(),
                            nextNonAbsDouble(),
                            nextNonAbsDouble(),
                            nextNonAbsDouble()
                    );
                }
            }
        });

        // Block Animate Tick
        registerBlockAnimateTickProvider(Blocks.REDSTONE_BLOCK, (state, level, pos, random) -> {
            if (INSTANCE.redstoneBlockDust.get()) {
                ParticleSpawnUtil.spawnParticlesAroundBlock(DustParticleOptions.REDSTONE, level, pos, random);
            }
        });
        registerBlockAnimateTickProvider(Blocks.GLOWSTONE, (state, level, pos, random) -> {
            if (INSTANCE.glowstoneBlockDust.get().equals(ModConfigs.GlowstoneDustSpawnType.OFF)
                    || (INSTANCE.glowstoneBlockDust.get().equals(ModConfigs.GlowstoneDustSpawnType.NETHER_ONLY)
                    && !level.dimension().equals(Level.NETHER))) {
                return;
            }

            ParticleSpawnUtil.spawnParticlesAroundBlock(Util.GLOWSTONE_DUST_PARTICLES, level, pos, random);
        });
        registerBlockAnimateTickProvider(Blocks.TORCHFLOWER, (state, level, pos, random) -> {
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
        registerBlockAnimateTickProvider(Blocks.DRAGON_EGG, (state, level, pos, random) -> {
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
        registerBlockAnimateTickProvider(Blocks.LAVA_CAULDRON, (state, level, pos, random) -> {
            if (INSTANCE.lavaCauldronSparks.get()) {
                ParticleSpawnUtil.spawnLavaSparks(level, pos.above(), random, 5);
            }
        });
        registerBlockAnimateTickProvider(Blocks.BEACON, (state, level, pos, random) -> {
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
        registerBlockAnimateTickProvider(Blocks.RESPAWN_ANCHOR, (state, level, pos, random) -> {
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
        registerBlockAnimateTickProvider(Blocks.FURNACE, (state, level, pos, random) -> {
            if (ModConfigs.INSTANCE.furnaceSparks.get() && state.getValue(FurnaceBlock.LIT)) {
                Direction direction = state.getValue(FurnaceBlock.FACING);
                Direction.Axis axis = direction.getAxis();
                ParticleSpawnUtil.spawnSparks(level, random, pos, new Vec3(0.5 + (0.6 * direction.getStepX()), random.nextDouble() * 6 / 16, 0.5 + (0.6 * direction.getStepZ())), new Vec3i(1, 1, 1), 3, axis == Direction.Axis.X ? 10 : 3, axis == Direction.Axis.Z ? 10 : 3, false, false);
            }
        });
        registerBlockAnimateTickProvider(Blocks.WATER_CAULDRON, (state, level, pos, random) -> {
            ParticleSpawnUtil.spawnHeatedWaterParticles(level, pos, random, false,
                    0.5625 + (state.getValue(LayeredCauldronBlock.LEVEL) * 0.1875),
                    INSTANCE.steamingWaterCauldron, INSTANCE.boilingWaterCauldron
            );
        });
    }

    private static <T extends Entity> void registerOnEntityHurtProvider(EntityType<T> type, EntityProvider<T> provider) {
        ON_ENTITY_HURT_PROVIDERS.put(type, provider);
    }

    private static void registerBlockAnimateTickProvider(Block block, BlockProvider provider) {
        BLOCK_ANIMATE_TICK_PROVIDERS.put(block, provider);
    }

    @SuppressWarnings("unchecked")
    public static <T extends LivingEntity> void entityHurt(T entity, Level level, RandomSource random) {
        EntityType<T> type = (EntityType<T>) entity.getType();
        if (ON_ENTITY_HURT_PROVIDERS.containsKey(type)) {
            ((EntityProvider<T>) ON_ENTITY_HURT_PROVIDERS.get(type)).apply(entity, level, random);
        }
    }

    public static void entityFell(LivingEntity entity, double y, float distance, int fallDamage) {
        if (entity.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
            return;
        }

        if (!INSTANCE.fallDamageDustClouds.get()) {
            return;
        }

        if (fallDamage <= 0 && !((entity instanceof AbstractHorse) && distance > (entity instanceof Camel ? 0.5 : 1))) {
            return;
        }

        if (entity.isInWater() || entity.isInLava() || entity.isInPowderSnow) {
            return;
        }

        Level level = entity.level();
        RandomSource random = entity.getRandom();

        if (entity instanceof Strider strider) {
            if (level.getFluidState(strider.getOnPos().atY(Mth.floor(y))).is(FluidTags.LAVA)) {
                return;
            }
        }

        if (fallDamage < 4) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(ModParticles.SMALL_DUST_CLOUD.get(),
                        entity.getRandomX(1),
                        y + Math.max(Math.min(random.nextFloat(), 0.5), 0.2),
                        entity.getRandomZ(1),
                        0.3 * nextSign(),
                        random.nextDouble(),
                        0.3 * nextSign()
                );
            }
            return;
        }

        for (int i = 0; i < 10; i++) {
            level.addParticle(ModParticles.LARGE_DUST_CLOUD.get(),
                    entity.getRandomX(1),
                    y + Math.max(Math.min(random.nextFloat(), 0.5), 0.2),
                    entity.getRandomZ(1),
                    0.5 * nextSign(),
                    random.nextDouble() * 3,
                    0.5 * nextSign()
            );
        }
    }

    public static void blockAnimateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Block block = state.getBlock();

        if (BLOCK_ANIMATE_TICK_PROVIDERS.containsKey(block)) {
            BLOCK_ANIMATE_TICK_PROVIDERS.get(block).apply(state, level, pos, random);
            return;
        }

        if (block instanceof LanternBlock) {
            if (INSTANCE.lanternSparks.get()) {
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
            }
        }
        else if (block instanceof CommandBlock
                && (INSTANCE.commandBlockParticles.get() == ModConfigs.CommandBlockSpawnType.ON
                || (INSTANCE.commandBlockParticles.get() == ModConfigs.CommandBlockSpawnType.NOT_CREATIVE
                && !Minecraft.getInstance().player.isCreative()))) {
            ParticleSpawnUtil.spawnCmdBlockParticles(level, Vec3.atCenterOf(pos), random, (direction, relativePos) ->
                    !Util.isSolidOrNotEmpty(level, BlockPos.containing(relativePos))
            );
        }
    }

    @FunctionalInterface
    public interface EntityProvider<T extends Entity> {

        void apply(T entity, Level level, RandomSource random);
    }

    @FunctionalInterface
    public interface BlockProvider {

        void apply(BlockState state, Level level, BlockPos pos, RandomSource random);
    }
}
