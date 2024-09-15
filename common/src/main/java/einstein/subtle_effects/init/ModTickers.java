package einstein.subtle_effects.init;

import einstein.subtle_effects.particle.option.BooleanParticleOptions;
import einstein.subtle_effects.tickers.*;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Predicate;

import static einstein.subtle_effects.init.ModConfigs.INSTANCE;
import static einstein.subtle_effects.tickers.TickerManager.registerSimpleTicker;
import static einstein.subtle_effects.tickers.TickerManager.registerTicker;
import static einstein.subtle_effects.util.MathUtil.nextDouble;
import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;

public class ModTickers {

    private static final Predicate<Entity> LOCAL_PLAYER = entity -> entity.equals(Minecraft.getInstance().player);

    public static void init() {
        registerTicker(entity -> entity instanceof LivingEntity, SleepingTicker::new);
        registerTicker(entity -> entity instanceof LivingEntity, EntityFireTicker::new);
        registerTicker(entity -> entity instanceof AbstractMinecart && INSTANCE.minecartLandingSparks.get(), MinecartSparksTicker::new);
        registerTicker(LOCAL_PLAYER.and(entity -> INSTANCE.stomachGrowling.get()), StomachGrowlingTicker::new);
        registerTicker(LOCAL_PLAYER.and(entity -> INSTANCE.mobSkullShaders.get()), MobSkullShaderTicker::new);
        registerTicker(LOCAL_PLAYER.and(entity -> INSTANCE.heartBeating.get()), HeartbeatTicker::new);
        registerTicker(entity -> entity.getType().equals(EntityType.SLIME) && INSTANCE.slimeTrails.get(), (Slime entity) -> new SlimeTrailTicker<>(entity, ModParticles.SLIME_TRAIL));
        registerTicker(entity -> entity.getType().equals(EntityType.MAGMA_CUBE) && INSTANCE.magmaCubeTrails.get(), (MagmaCube entity) -> new SlimeTrailTicker<>(entity, ModParticles.MAGMA_CUBE_TRAIL));
        registerTicker(entity -> entity.getType().equals(EntityType.IRON_GOLEM) && INSTANCE.ironGolemCrackParticles.get(), IronGolemTicker::new);

        registerSimpleTicker(entity -> entity instanceof ItemEntity itemEntity
                        && INSTANCE.itemRarityParticles.get() != ModConfigs.ItemRaritySpawnType.OFF
                        && !(itemEntity.getItem().getRarity() == Rarity.COMMON
                        && INSTANCE.itemRarityParticles.get() == ModConfigs.ItemRaritySpawnType.NOT_COMMON),
                (entity, level, random) -> {
                    level.addParticle(new ItemParticleOption(ModParticles.ITEM_RARITY.get(), ((ItemEntity) entity).getItem()),
                            entity.getRandomX(1),
                            entity.getY(),
                            entity.getRandomZ(1),
                            0,
                            nextDouble(random, 0.02),
                            0
                    );
                });
        registerSimpleTicker(entity -> entity instanceof Player && INSTANCE.sprintingDustClouds.get(), (entity, level, random) -> {
            Player player = (Player) entity;
            if (player.canSpawnSprintParticle() && player.onGround() && !player.isUsingItem()) {
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
        });
        registerSimpleTicker(entity -> entity instanceof FallingBlockEntity && INSTANCE.fallingBlockDust.get(), (entity, level, random) -> {
            FallingBlockEntity fallingBlock = (FallingBlockEntity) entity;
            if (!fallingBlock.onGround() && !fallingBlock.isNoGravity()) {
                BlockState state = fallingBlock.getBlockState();
                if (INSTANCE.fallingBlockDustBlocks.get().contains(BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString())) {
                    level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), entity.getRandomX(1), entity.getY() + 0.05, entity.getRandomZ(1), 0, 0, 0);
                }
            }
        });
        registerSimpleTicker(EntityType.SNOWBALL, (entity, level, random) -> {
            if (shouldSpawn(random, INSTANCE.snowballTrailDensity)) {
                Vec3 deltaMovement = entity.getDeltaMovement();
                level.addParticle(ModParticles.SNOWBALL_TRAIL.get(), entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), deltaMovement.x * 0.5, deltaMovement.y, deltaMovement.z * 0.5);
            }
        });
        registerSimpleTicker(EntityType.ENDER_PEARL, (entity, level, random) -> {
            if (INSTANCE.enderPearlTrail.get()) {
                for (int i = 0; i < 10; i++) {
                    level.addParticle(ParticleTypes.PORTAL, entity.getRandomX(2), entity.getRandomY(), entity.getRandomZ(2), 0, 0, 0);
                }
            }
        });
        registerSimpleTicker(EntityType.ALLAY, (entity, level, random) -> {
            if (shouldSpawn(random, INSTANCE.allayMagicDensity)) {
                level.addParticle(ModParticles.ALLAY_MAGIC.get(),
                        entity.getRandomX(1),
                        entity.getRandomY(),
                        entity.getRandomZ(1),
                        nextNonAbsDouble(random, 0.04),
                        0,
                        nextNonAbsDouble(random, 0.04)
                );
            }
        });
        registerSimpleTicker(EntityType.VEX, (entity, level, random) -> {
            if (shouldSpawn(random, INSTANCE.vexMagicDensity)) {
                level.addParticle(new BooleanParticleOptions(ModParticles.VEX_MAGIC.get(), entity.isCharging()),
                        entity.getRandomX(1),
                        entity.getRandomY(),
                        entity.getRandomZ(1),
                        nextNonAbsDouble(random, 0.04),
                        0,
                        nextNonAbsDouble(random, 0.04)
                );
            }
        });
        registerSimpleTicker(EntityType.CAMEL, (entity, level, random) -> {
            if (entity.isDashing() && entity.onGround()) {
                for (int i = 0; i < 10; i++) {
                    ParticleSpawnUtil.spawnCreatureMovementDustClouds(entity, level, random, 5);
                }
            }
        });
        registerSimpleTicker(EntityType.DRAGON_FIREBALL, (entity, level, random) -> {
            if (INSTANCE.improvedDragonFireballTrail.get()) {
                for (int i = 0; i < 10; i++) {
                    level.addParticle(ParticleTypes.DRAGON_BREATH, entity.getRandomX(2), entity.getRandomY(), entity.getRandomZ(2), 0, 0, 0);
                }
            }
        });
        registerSimpleTicker(EntityType.COMMAND_BLOCK_MINECART, (entity, level, random) -> {
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
        registerSimpleTicker(EntityType.TNT, (entity, level, random) -> {
            if (INSTANCE.litTntSparks.get()) {
                level.addParticle(ModParticles.SHORT_SPARK.get(),
                        entity.getRandomX(0.5),
                        entity.getY(1),
                        entity.getRandomZ(0.5),
                        nextNonAbsDouble(random, 0.01),
                        nextNonAbsDouble(random, 0.01),
                        nextNonAbsDouble(random, 0.01)
                );
            }

            if (INSTANCE.litTntFlames.get() && random.nextInt(10) == 0) {
                level.addParticle(ParticleTypes.FLAME,
                        entity.getX(),
                        entity.getY(1.1),
                        entity.getZ(),
                        0, 0, 0
                );
            }
        });
        registerSimpleTicker(EntityType.END_CRYSTAL, (entity, level, random) -> {
            if (INSTANCE.endCrystalParticles.get()) {
                if (level.getBlockState(entity.blockPosition()).getBlock() instanceof BaseFireBlock || random.nextInt(3) == 0) {
                    level.addParticle(ModParticles.END_CRYSTAL.get(),
                            entity.getRandomX(1),
                            entity.getRandomY() + nextNonAbsDouble(random),
                            entity.getRandomZ(1),
                            0, 0, 0
                    );
                }
            }
        });
        registerSimpleTicker(EntityType.SPECTRAL_ARROW, (entity, level, random) -> {
            if (random.nextInt(3) == 0) {
                level.addParticle(Util.GLOWSTONE_DUST_PARTICLES,
                        entity.getRandomX(1),
                        entity.getRandomY(),
                        entity.getRandomZ(1),
                        0, 0, 0
                );
            }
        });
    }

    private static boolean shouldSpawn(RandomSource random, ModConfigSpec.DoubleValue chanceConfig) {
        return Math.min(random.nextFloat(), 1) < chanceConfig.get();
    }
}
