package einstein.ambient_sleep.util;

import einstein.ambient_sleep.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;

import static einstein.ambient_sleep.init.ModConfigs.INSTANCE;

public class ParticleManager {

    public static void entityTick(Entity entity, Level level, RandomSource random) {
        EntityType<?> type = entity.getType();
        if (type.equals(EntityType.ENDER_PEARL) && INSTANCE.enderPearlTrail.get()) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.PORTAL, entity.getRandomX(2), entity.getRandomY(), entity.getRandomZ(2), 0, 0, 0);
            }
        }
        else if (type.equals(EntityType.SNOWBALL)) {
            if (shouldSpawn(random, INSTANCE.snowballTrailChance)) {
                Vec3 deltaMovement = entity.getDeltaMovement();
                level.addParticle(ModParticles.SNOWBALL_TRAIL.get(), entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), deltaMovement.x * 0.5, deltaMovement.y, deltaMovement.z * 0.5);
            }
        }
        else if (type.equals(EntityType.ALLAY)) {
            if (shouldSpawn(random, INSTANCE.allayMagicChance)) {
                level.addParticle(ModParticles.ALLAY_MAGIC.get(),
                        entity.getRandomX(1),
                        entity.getRandomY(),
                        entity.getRandomZ(1),
                        random.nextInt(4) / 100D * (random.nextBoolean() ? 1 : -1),
                        0,
                        random.nextInt(4) / 100D * (random.nextBoolean() ? 1 : -1)
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
                level.addParticle(ModParticles.CHICKEN_FEATHER.get(), entity.getX(), entity.getY(), entity.getZ(), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1));
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
                level.addParticle(particle, entity.getX(), entity.getY(), entity.getZ(), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1));
            }
        }
    }

    public static void entityFell(LivingEntity entity, double y, int fallDamage) {
        Level level = entity.level();
        RandomSource random = entity.getRandom();
        if (entity.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE) || fallDamage <= 0) {
            return;
        }

        if (!INSTANCE.fallDamageDustClouds.get()) {
            return;
        }

        if (fallDamage < 4) {
            for (int i = 0; i < 5; i++) {
                double x = entity.getRandomX(1);
                double z = entity.getRandomZ(1);
                level.addParticle(ModParticles.SMALL_DUST_CLOUD.get(), x, y + Math.min(random.nextFloat(), 0.4), z, 0.3 * (random.nextBoolean() ? 1 : -1), random.nextDouble() * 1.5, 0.3 * (random.nextBoolean() ? 1 : -1));
            }
            return;
        }

        for (int i = 0; i < 10; i++) {
            double x = entity.getRandomX(1);
            double z = entity.getRandomZ(1);
            level.addParticle(ModParticles.LARGE_DUST_CLOUD.get(), x, y + Math.min(random.nextFloat(), 0.4), z, 0.5 * (random.nextBoolean() ? 1 : -1), random.nextDouble() * 3, 0.5 * (random.nextBoolean() ? 1 : -1));
        }
    }

    // Only works for blocks that don't already implement animateTick
    public static void blockAnimateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.is(Blocks.REDSTONE_BLOCK) && INSTANCE.redstoneBlockDust.get()) {
            Util.spawnParticlesAroundBlock(DustParticleOptions.REDSTONE, level, pos, random);
        }
        else if (state.is(Blocks.GLOWSTONE) && INSTANCE.glowstoneBlockDust.get()) {
            Util.spawnParticlesAroundBlock(Util.GLOWSTONE_DUST_PARTICLES, level, pos, random);
        }
    }
}
