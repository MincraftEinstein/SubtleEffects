package einstein.ambient_sleep.util;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.particle.option.BooleanParticleOptions;
import einstein.ambient_sleep.particle.option.PositionParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

import static einstein.ambient_sleep.init.ModConfigs.INSTANCE;
import static einstein.ambient_sleep.util.MathUtil.*;

public class ParticleManager {

    private static final Map<EntityType<?>, EntityProvider<?>> ENTITY_TICK_PROVIDERS = new HashMap<>();
    private static final Map<EntityType<?>, EntityProvider<?>> ON_ENTITY_HURT_PROVIDERS = new HashMap<>();
    private static final Map<Block, BlockProvider> BLOCK_ANIMATE_TICK_PROVIDERS = new HashMap<>();

    public static void init() {
        // Entity Tick
        registerEntityTickProvider(EntityType.SNOWBALL, (entity, level, random) -> {
            if (shouldSpawn(random, INSTANCE.snowballTrailDensity)) {
                Vec3 deltaMovement = entity.getDeltaMovement();
                level.addParticle(ModParticles.SNOWBALL_TRAIL.get(), entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), deltaMovement.x * 0.5, deltaMovement.y, deltaMovement.z * 0.5);
            }
        });
        registerEntityTickProvider(EntityType.ENDER_PEARL, (entity, level, random) -> {
            if (INSTANCE.enderPearlTrail.get()) {
                for (int i = 0; i < 10; i++) {
                    level.addParticle(ParticleTypes.PORTAL, entity.getRandomX(2), entity.getRandomY(), entity.getRandomZ(2), 0, 0, 0);
                }
            }
        });
        registerEntityTickProvider(EntityType.ALLAY, (entity, level, random) -> {
            if (shouldSpawn(random, INSTANCE.allayMagicDensity)) {
                level.addParticle(ModParticles.ALLAY_MAGIC.get(),
                        entity.getRandomX(1),
                        entity.getRandomY(),
                        entity.getRandomZ(1),
                        nextFloat(4) * nextSign(),
                        0,
                        nextFloat(4) * nextSign()
                );
            }
        });
        registerEntityTickProvider(EntityType.VEX, (entity, level, random) -> {
            if (shouldSpawn(random, INSTANCE.vexMagicDensity)) {
                level.addParticle(new BooleanParticleOptions(ModParticles.VEX_MAGIC.get(), entity.isCharging()),
                        entity.getRandomX(1),
                        entity.getRandomY(),
                        entity.getRandomZ(1),
                        nextFloat(4) * nextSign(),
                        0,
                        nextFloat(4) * nextSign()
                );
            }
        });
        registerEntityTickProvider(EntityType.CAMEL, (entity, level, random) -> {
            if (entity.isDashing() && entity.onGround()) {
                for (int i = 0; i < 10; i++) {
                    ParticleSpawnUtil.spawnCreatureMovementDustClouds(entity, level, random, 5);
                }
            }
        });
        registerEntityTickProvider(EntityType.RAVAGER, (entity, level, random) -> {

        });
        registerEntityTickProvider(EntityType.DRAGON_FIREBALL, (entity, level, random) -> {
            if (INSTANCE.improvedDragonFireballTrail.get()) {
                for (int i = 0; i < 10; i++) {
                    level.addParticle(ParticleTypes.DRAGON_BREATH, entity.getRandomX(2), entity.getRandomY(), entity.getRandomZ(2), 0, 0, 0);
                }
            }
        });
        registerEntityTickProvider(EntityType.COMMAND_BLOCK_MINECART, (entity, level, random) -> {
            if (INSTANCE.commandBlockMinecartParticles.get() == ModConfigs.CommandBlockSpawnType.ON
                    || (INSTANCE.commandBlockMinecartParticles.get() == ModConfigs.CommandBlockSpawnType.NOT_CREATIVE
                    && !Minecraft.getInstance().player.isCreative())) {
                if (random.nextInt(10) == 0) {
                    ParticleSpawnUtil.spawnCmdBlockParticles(level, entity.position()
                                    // The vanilla calculation the command block's rendered location + 1 block (16) / 75 the (scale of the rendered command block) / .5 to get the center of the command block
                                    .add(0, (double) -(entity.getDisplayOffset() - 8) / 16 + (((double) 16 / 75) / 0.5), 0),
                            random, (direction, relativePos) -> true
                    );
                }
            }
        });

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
    }

    private static <T extends Entity> void registerEntityTickProvider(EntityType<T> type, EntityProvider<T> provider) {
        ENTITY_TICK_PROVIDERS.put(type, provider);
    }

    private static <T extends Entity> void registerOnEntityHurtProvider(EntityType<T> type, EntityProvider<T> provider) {
        ON_ENTITY_HURT_PROVIDERS.put(type, provider);
    }

    private static void registerBlockAnimateTickProvider(Block block, BlockProvider provider) {
        BLOCK_ANIMATE_TICK_PROVIDERS.put(block, provider);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> void entityTick(T entity, Level level, RandomSource random) {
        if (!level.isClientSide) {
            return;
        }

        float bbWidth = entity.getBbWidth();
        float bbHeight = entity.getBbHeight();

        if (entity.isOnFire()) {
            if (random.nextInt(90) == 0 && INSTANCE.burningEntitySounds.get()) {
                Util.playClientSound(entity.getSoundSource(), entity, SoundEvents.FIRE_EXTINGUISH, 0.3F, 1);
            }

            if (bbWidth <= 4 && bbHeight <= 4) {
                if (INSTANCE.burningEntitySmoke.get() != ModConfigs.SmokeType.OFF) {
                    level.addParticle(INSTANCE.burningEntitySmoke.get().getParticle().get(), entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), 0, 0, 0);
                }

                if (INSTANCE.burningEntitySparks.get()) {
                    for (int i = 0; i < 2; i++) {
                        level.addParticle(ModParticles.SHORT_SPARK.get(), entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1),
                                nextFloat(3) * nextSign(),
                                nextFloat(5) * nextSign(),
                                nextFloat(3) * nextSign()
                        );
                    }
                }

                if (bbWidth < 2 && bbHeight < 2 && !random.nextBoolean()) {
                    return;
                }

                if (INSTANCE.burningEntityFlames.get()) {
                    level.addParticle(ParticleTypes.FLAME, entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), 0, 0, 0);
                }
            }
        }

        EntityType<T> type = (EntityType<T>) entity.getType();
        if (ENTITY_TICK_PROVIDERS.containsKey(type)) {
            ((EntityProvider<T>) ENTITY_TICK_PROVIDERS.get(type)).apply(entity, level, random);
            return;
        }

        if (entity instanceof ItemEntity itemEntity && INSTANCE.itemRarityParticles.get() != ModConfigs.ItemRaritySpawnType.OFF) {
            if (INSTANCE.itemRarityParticles.get() == ModConfigs.ItemRaritySpawnType.NOT_COMMON
                    && itemEntity.getItem().getRarity() == Rarity.COMMON) {
                return;
            }
            level.addParticle(new ItemParticleOption(ModParticles.ITEM_RARITY.get(), itemEntity.getItem()),
                    entity.getRandomX(1),
                    entity.getY(),
                    entity.getRandomZ(1),
                    0,
                    MathUtil.nextFloat(2),
                    0
            );
        }
        else if (entity instanceof Player player) {
            if (INSTANCE.sprintingDustClouds.get() && player.canSpawnSprintParticle() && player.onGround() && !player.isUsingItem()) {
                if (random.nextBoolean()) {
                    level.addParticle(ModParticles.SMALL_DUST_CLOUD.get(),
                            entity.getRandomX(1),
                            entity.getY() + Math.max(Math.min(random.nextFloat(), 0.3), 0.2),
                            entity.getRandomZ(1),
                            0,
                            random.nextDouble(),
                            0
                    );
                }
            }
        }
        else if (entity instanceof FallingBlockEntity fallingBlock && INSTANCE.fallingBlockDust.get()) {
            if (!fallingBlock.onGround() && !fallingBlock.isNoGravity()) {
                BlockState state = fallingBlock.getBlockState();
                if (INSTANCE.fallingBlockDustBlocks.get().contains(BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString())) {
                    level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), entity.getRandomX(1), entity.getY() + 0.05, entity.getRandomZ(1), 0, 0, 0);
                }
            }
        }
    }

    private static boolean shouldSpawn(RandomSource random, ForgeConfigSpec.DoubleValue chanceConfig) {
        return Math.min(random.nextFloat(), 1) < chanceConfig.get();
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

    // Only works for blocks that don't already implement animateTick
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
