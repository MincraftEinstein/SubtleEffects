package einstein.subtle_effects.init;

import einstein.subtle_effects.configs.CommandBlockSpawnType;
import einstein.subtle_effects.configs.ModEntityConfigs;
import einstein.subtle_effects.configs.items.ItemRarityConfigs;
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.particle.option.BooleanParticleOptions;
import einstein.subtle_effects.tickers.entity_tickers.*;
import einstein.subtle_effects.tickers.entity_tickers.sleeping.*;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.SparkType;
import einstein.subtle_effects.util.Util;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

import static einstein.subtle_effects.init.ModConfigs.*;
import static einstein.subtle_effects.tickers.entity_tickers.EntityTickerManager.register;
import static einstein.subtle_effects.tickers.entity_tickers.EntityTickerManager.registerSimple;
import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;

public class ModEntityTickers {

    private static final Predicate<Entity> LOCAL_PLAYER = entity -> entity.equals(Minecraft.getInstance().player);

    public static void init() {
        register(entity -> true, EntityCauldronTicker::new);
        register(entity -> !(entity instanceof LightningBolt), EntityFireTicker::new);
        register(entity -> entity instanceof LivingEntity, ModEntityTickers::getSleepingTicker);
        register(entity -> entity instanceof AbstractMinecart && ENTITIES.minecartSparksDisplayType != ModEntityConfigs.MinecartSparksDisplayType.OFF, MinecartSparksTicker::new);
        register(LOCAL_PLAYER.and(entity -> ENTITIES.humanoids.player.stomachGrowlingThreshold.get() > 0), StomachGrowlingTicker::new);
        register(LOCAL_PLAYER.and(entity -> ModConfigs.GENERAL.mobSkullShaders), MobSkullShaderTicker::new);
        register(LOCAL_PLAYER.and(entity -> ENTITIES.humanoids.player.heartBeatingThreshold.get() > 0), HeartbeatTicker::new);
        register(entity -> isHumanoid(entity, !entity.level().dimension().equals(Level.NETHER)) && ENTITIES.humanoids.drowningBubblesDisplayType.isEnabled(), DrowningTicker::new);
        register(entity -> isHumanoid(entity, !entity.level().dimension().equals(Level.NETHER)) && ENTITIES.humanoids.frostyBreath.displayType.isEnabled(), FrostyBreathTicker::new);
        register(entity -> entity.getType().equals(EntityType.SLIME) && ENTITIES.slimeTrails, (Slime entity) -> new SlimeTrailTicker<>(entity, ModParticles.SLIME_TRAIL));
        register(entity -> entity.getType().equals(EntityType.MAGMA_CUBE) && ENTITIES.magmaCubeTrails, (MagmaCube entity) -> new SlimeTrailTicker<>(entity, ModParticles.MAGMA_CUBE_TRAIL));
        register(entity -> entity.getType().equals(EntityType.IRON_GOLEM) && ENTITIES.ironGolemCrackParticles, IronGolemTicker::new);
        register(entity -> entity instanceof ItemEntity && ITEMS.itemRarity.particlesDisplayType != ItemRarityConfigs.DisplayType.OFF, ItemRarityTicker::new);
        register(entity -> entity instanceof Witch && ENTITIES.humanoids.NPCsHavePotionRings && ENTITIES.humanoids.potionRingsDisplayType.isEnabled(), WitchPotionRingTicker::new);
        register(entity -> isNPC(entity, true) && ENTITIES.humanoids.NPCsHavePotionRings && ENTITIES.humanoids.potionRingsDisplayType.isEnabled(), (LivingEntity entity) -> new HumanoidPotionRingTicker<>(entity));

        registerSimple(entity -> entity instanceof Player && ENTITIES.dustClouds.playerRunning, true,
                (entity, level, random) -> {

                    if (ENTITIES.dustClouds.preventWhenRaining && level.isRainingAt(entity.blockPosition())) {
                        return;
                    }

                    Player player = (Player) entity;

                    if (ENTITIES.dustClouds.playerRunningRequiresSpeed && !player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                        return;
                    }

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
        registerSimple(entity -> entity instanceof FallingBlockEntity && BLOCKS.fallingBlocks.fallingDust, false, (entity, level, random) -> {
            FallingBlockEntity fallingBlock = (FallingBlockEntity) entity;

            int startDistance = BLOCKS.fallingBlocks.fallingDustStartDistance.get();
            float fallDistance = fallingBlock.fallDistance;

            if (fallDistance <= startDistance) {
                return;
            }

            if (!fallingBlock.onGround() && !fallingBlock.isNoGravity()) {
                BlockState state = fallingBlock.getBlockState();
                Block block = state.getBlock();

                if (BLOCKS.fallingBlocks.dustyBlocks.contains(block)) {
                    double size = startDistance > 0 ? fallDistance <= startDistance + ((startDistance / 3F) * 2) ? 0.5 : 1 : 1;

                    for (int i = 0 ; i < (size == 1 ? 2 : 1); i++) {
                        level.addParticle(
                                new BlockParticleOption(ParticleTypes.FALLING_DUST,
                                        block instanceof BrushableBlock brushableBlock ?
                                                brushableBlock.getTurnsInto().defaultBlockState() : state),
                                entity.getRandomX(size),
                                entity.getY() + 0.05,
                                entity.getRandomZ(size),
                                0, 0, 0
                        );
                    }
                }
            }
        });
        registerSimple(EntityType.SNOWBALL, false, () -> ITEMS.projectiles.snowballTrailDensity.get() > 0, (entity, level, random) -> {
            if (shouldSpawn(random, ITEMS.projectiles.snowballTrailDensity)) {
                Vec3 deltaMovement = entity.getDeltaMovement();
                level.addParticle(ModParticles.SNOWBALL_TRAIL.get(),
                        entity.getRandomX(1),
                        entity.getRandomY(),
                        entity.getRandomZ(1),
                        deltaMovement.x * 0.5,
                        deltaMovement.y,
                        deltaMovement.z * 0.5
                );
            }
        });
        registerSimple(EntityType.ENDER_PEARL, false, () -> ITEMS.projectiles.enderPearlTrail, (entity, level, random) -> {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.PORTAL,
                        entity.getRandomX(2),
                        entity.getRandomY(),
                        entity.getRandomZ(2),
                        0, 0, 0
                );
            }
        });
        registerSimple(EntityType.ALLAY, true, () -> ENTITIES.allayMagicDensity.get() > 0, (entity, level, random) -> {
            if (shouldSpawn(random, ENTITIES.allayMagicDensity)) {
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
        registerSimple(EntityType.VEX, true, () -> ENTITIES.vexMagicDensity.get() > 0, (entity, level, random) -> {
            if (shouldSpawn(random, ENTITIES.vexMagicDensity)) {
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
        registerSimple(EntityType.CAMEL, true, () -> ENTITIES.dustClouds.mobRunning, (entity, level, random) -> {
            if (entity.isDashing() && entity.onGround()) {
                for (int i = 0; i < 10; i++) {
                    ParticleSpawnUtil.spawnCreatureMovementDustCloudsNoConfig(entity, level, random, 5);
                }
            }
        });
        registerSimple(EntityType.DRAGON_FIREBALL, false, () -> ENTITIES.improvedDragonFireballTrail, (entity, level, random) -> {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.DRAGON_BREATH,
                        entity.getRandomX(2),
                        entity.getRandomY(),
                        entity.getRandomZ(2),
                        0, 0, 0
                );
            }
        });
        registerSimple(EntityType.COMMAND_BLOCK_MINECART, false, () -> ENTITIES.commandBlockMinecartParticles != CommandBlockSpawnType.OFF,
                (entity, level, random) -> {
                    if (ENTITIES.commandBlockMinecartParticles.canTick()) {
                        if (random.nextInt(10) == 0) {
                            ParticleSpawnUtil.spawnCmdBlockParticles(level, entity.position()
                                            // The vanilla calculation of the command block's rendered location + 1 block (16) / 75 (the scale of the rendered command block) / .5 (to get the center of the command block)
                                            .add(0, -(entity.getDisplayOffset() - 8) / 16D + ((16 / 75D) / 0.5D), 0),
                                    random, (direction, relativePos) -> true
                            );
                        }
                    }
                });
        registerSimple(EntityType.TNT, false, () -> ENTITIES.explosives.tntSparks, (entity, level, random) -> {
            level.addParticle(SparkParticle.create(SparkType.SHORT_LIFE, random),
                    entity.getRandomX(0.5),
                    entity.getY(1),
                    entity.getRandomZ(0.5),
                    nextNonAbsDouble(random, 0.01),
                    nextNonAbsDouble(random, 0.01),
                    nextNonAbsDouble(random, 0.01)
            );
        });
        registerSimple(EntityType.TNT, false, () -> ENTITIES.explosives.tntFlamesDensity.get() > 0, (entity, level, random) -> {
            if (random.nextInt(10) == 0) {
                int density = ENTITIES.explosives.tntFlamesDensity.get();
                if (density == 1) {
                    level.addParticle(ParticleTypes.FLAME,
                            entity.getX(),
                            entity.getY(1.1),
                            entity.getZ(),
                            0, 0, 0
                    );
                    return;
                }

                for (int i = 0; i < density; i++) {
                    level.addParticle(ParticleTypes.FLAME,
                            entity.getRandomX(0.7),
                            entity.getRandomY(),
                            entity.getRandomZ(0.7),
                            0, 0, 0
                    );
                }
            }
        });
        registerSimple(EntityType.END_CRYSTAL, false, () -> ENTITIES.endCrystalParticles, (entity, level, random) -> {
            if (level.getBlockState(entity.blockPosition()).getBlock() instanceof BaseFireBlock || random.nextInt(3) == 0) {
                level.addParticle(ModParticles.END_CRYSTAL.get(),
                        entity.getRandomX(1),
                        entity.getRandomY() + nextNonAbsDouble(random),
                        entity.getRandomZ(1),
                        0, 0, 0
                );
            }
        });
        registerSimple(EntityType.SPECTRAL_ARROW, false, () -> ENTITIES.spectralArrowParticles, (entity, level, random) -> {
            if (random.nextInt(3) == 0) {
                level.addParticle(Util.GLOWSTONE_DUST_PARTICLES,
                        entity.getRandomX(1),
                        entity.getRandomY(),
                        entity.getRandomZ(1),
                        0, 0, 0
                );
            }
        });
        registerSimple(EntityType.CREEPER, true, () -> ENTITIES.explosives.creeperSparks, (entity, level, random) -> {
            if (entity.isIgnited()) {
                for (int i = 0; i < 3; i++) {
                    level.addParticle(SparkParticle.create(SparkType.SHORT_LIFE, random),
                            entity.getRandomX(1),
                            entity.getRandomY(),
                            entity.getRandomZ(1),
                            nextNonAbsDouble(random, 0.01),
                            nextNonAbsDouble(random, 0.01),
                            nextNonAbsDouble(random, 0.01)
                    );
                }
            }
        });
        registerSimple(EntityType.CREEPER, true, () -> ENTITIES.explosives.creeperSmoke.isEnabled(), (entity, level, random) -> {
            if (entity.isIgnited()) {
                level.addParticle(ENTITIES.explosives.creeperSmoke.getParticle().get(),
                        entity.getRandomX(1),
                        entity.getRandomY(),
                        entity.getRandomZ(1),
                        nextNonAbsDouble(random, 0.01),
                        nextNonAbsDouble(random, 0.01),
                        nextNonAbsDouble(random, 0.01)
                );
            }
        });
        registerSimple(entity -> entity instanceof LivingEntity && entity.canFreeze() && ENTITIES.freezingSnowFlakes, false, (entity, level, random) -> {
            if (entity.isFreezing() || entity.getTicksFrozen() > 0 || entity.getInBlockState().is(Blocks.POWDER_SNOW_CAULDRON)) {
                level.addParticle(ModParticles.FREEZING.get(),
                        entity.getRandomX(1),
                        entity.getRandomY(),
                        entity.getRandomZ(1),
                        0, 0, 0
                );
            }
        });
    }

    public static boolean shouldSpawn(RandomSource random, ValidatedDouble chanceConfig) {
        return Math.min(random.nextFloat(), 1) < chanceConfig.get();
    }

    private static SleepingTicker<?> getSleepingTicker(LivingEntity entity) {
        return switch (entity) {
            case AbstractVillager villager -> new VillagerSleepingTicker(villager);
            case Player player -> new PlayerSleepingTicker(player);
            case Bat bat -> new BatSleepingTicker(bat);
            case Cat cat -> new CatSleepingTicker(cat);
            case Fox fox -> new FoxSleepingTicker(fox);
            default -> new SleepingTicker<>(entity);
        };
    }

    private static boolean isHumanoid(Entity entity, boolean includePiglins) {
        return entity instanceof Player
                || isNPC(entity, includePiglins)
                || entity instanceof Witch;
    }

    private static boolean isNPC(Entity entity, boolean includePiglins) {
        return entity instanceof AbstractVillager
                || entity instanceof AbstractIllager
                || (includePiglins && entity instanceof AbstractPiglin);
    }
}
