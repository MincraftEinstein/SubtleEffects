package einstein.ambient_sleep.util;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.init.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;

import static einstein.ambient_sleep.init.ModConfigs.INSTANCE;
import static einstein.ambient_sleep.util.MathUtil.*;

public class ParticleManager {

    public static void entityTick(Entity entity, Level level, RandomSource random) {
        if (!level.isClientSide) {
            return;
        }

        float bbWidth = entity.getBbWidth();
        float bbHeight = entity.getBbHeight();

        if (bbWidth <= 4 && bbHeight <= 4 && entity.isOnFire()) {
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

        EntityType<?> type = entity.getType();
        if (entity instanceof ItemEntity itemEntity) {
            level.addParticle(new ItemParticleOption(ModParticles.ITEM_RARITY.get(), itemEntity.getItem()),
                    entity.getRandomX(1),
                    entity.getY(),
                    entity.getRandomZ(1),
                    0,
                    MathUtil.nextFloat(2),
                    0
            );
        }
        else if (type.equals(EntityType.ENDER_PEARL) && INSTANCE.enderPearlTrail.get()) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.PORTAL, entity.getRandomX(2), entity.getRandomY(), entity.getRandomZ(2), 0, 0, 0);
            }
        }
        else if (type.equals(EntityType.SNOWBALL)) {
            if (shouldSpawn(random, INSTANCE.snowballTrailDensity)) {
                Vec3 deltaMovement = entity.getDeltaMovement();
                level.addParticle(ModParticles.SNOWBALL_TRAIL.get(), entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), deltaMovement.x * 0.5, deltaMovement.y, deltaMovement.z * 0.5);
            }
        }
        else if (type.equals(EntityType.ALLAY)) {
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
        }
        else if (type.equals(EntityType.CAMEL)) {
            Camel camel = (Camel) entity;
            if (camel.isDashing() && camel.onGround()) {
                for (int i = 0; i < 10; i++) {
                    ParticleSpawnUtil.spawnCreatureMovementDustClouds(camel, level, random, 5);
                }
            }
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
        else if (type.equals(EntityType.RAVAGER)) {
            Ravager ravager = (Ravager) entity;
            if (ravager.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() > 0.34D && ravager.onGround()) {
                ParticleSpawnUtil.spawnCreatureMovementDustClouds(ravager, level, random, 20);
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
        else if (type.equals(EntityType.DRAGON_FIREBALL) && INSTANCE.improvedDragonFireballTrail.get()) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.DRAGON_BREATH, entity.getRandomX(2), entity.getRandomY(), entity.getRandomZ(2), 0, 0, 0);
            }
        }
        else if (entity instanceof MinecartCommandBlock minecart
                && (INSTANCE.commandBlockMinecartParticles.get() == ModConfigs.CommandBlockSpawnType.ON
                || (INSTANCE.commandBlockMinecartParticles.get() == ModConfigs.CommandBlockSpawnType.NOT_CREATIVE
                && !Minecraft.getInstance().player.isCreative()))) {
            if (random.nextInt(10) == 0) {
                ParticleSpawnUtil.spawnCmdBlockParticles(level, entity.position()
                                // The vanilla calculation the command block's rendered location + 1 block (16) / 75 the (scale of the rendered command block) / .5 to get the center of the command block
                                .add(0, (double) -(minecart.getDisplayOffset() - 8) / 16 + (((double) 16 / 75) / 0.5), 0),
                        random, (direction, relativePos) -> true
                );
            }
        }
    }

    private static boolean shouldSpawn(RandomSource random, ForgeConfigSpec.DoubleValue chanceConfig) {
        return Math.min(random.nextFloat(), 1) < chanceConfig.get();
    }

    public static void entityHurt(LivingEntity entity, Level level, RandomSource random) {
        EntityType<?> type = entity.getType();
        if (type.equals(EntityType.CHICKEN) && INSTANCE.chickenHitFeathers.get()) {
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
        else if (type.equals(EntityType.PARROT) && INSTANCE.parrotHitFeathers.get()) {
            ParticleOptions particle = switch (((Parrot) entity).getVariant()) {
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
        else if (type.equals(EntityType.SNOW_GOLEM) && INSTANCE.snowGolemHitSnowflakes.get()) {
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
        else if (state.is(Blocks.REDSTONE_BLOCK) && INSTANCE.redstoneBlockDust.get()) {
            ParticleSpawnUtil.spawnParticlesAroundBlock(DustParticleOptions.REDSTONE, level, pos, random);
        }
        else if (state.is(Blocks.GLOWSTONE)) {
            if (INSTANCE.glowstoneBlockDust.get().equals(ModConfigs.GlowstoneDustSpawnType.OFF)
                    || (INSTANCE.glowstoneBlockDust.get().equals(ModConfigs.GlowstoneDustSpawnType.NETHER_ONLY)
                    && !level.dimension().equals(Level.NETHER))) {
                return;
            }

            ParticleSpawnUtil.spawnParticlesAroundBlock(Util.GLOWSTONE_DUST_PARTICLES, level, pos, random);
        }
        else if (state.is(Blocks.TORCHFLOWER)) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.8;
            double z = pos.getZ() + 0.5;

            if (INSTANCE.torchflowerSmoke.get() != ModConfigs.SmokeType.OFF && random.nextInt(3) == 0) {
                level.addParticle(INSTANCE.torchflowerSmoke.get().getParticle().get(), x, y, z, 0, 0, 0);
            }

            if (INSTANCE.torchflowerFlames.get() && random.nextInt(5) == 0) {
                level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
            }
        }
        else if (state.is(Blocks.DRAGON_EGG) && INSTANCE.dragonEggParticles.get()) {
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
        else if (state.is(Blocks.LAVA_CAULDRON) && INSTANCE.lavaCauldronSparks.get()) {
            ParticleSpawnUtil.spawnLavaSparks(level, pos.above(), random, 5);
        }
        else if ((state.is(Blocks.COMMAND_BLOCK)
                || state.is(Blocks.REPEATING_COMMAND_BLOCK)
                || state.is(Blocks.CHAIN_COMMAND_BLOCK))
                && (INSTANCE.commandBlockParticles.get() == ModConfigs.CommandBlockSpawnType.ON
                || (INSTANCE.commandBlockParticles.get() == ModConfigs.CommandBlockSpawnType.NOT_CREATIVE
                && !Minecraft.getInstance().player.isCreative()))) {
            ParticleSpawnUtil.spawnCmdBlockParticles(level, Vec3.atCenterOf(pos), random, (direction, relativePos) ->
                    !Util.isSolidOrNotEmpty(level, BlockPos.containing(relativePos))
            );
        }
    }
}
