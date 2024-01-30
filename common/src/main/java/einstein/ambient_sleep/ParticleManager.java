package einstein.ambient_sleep;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ParticleManager {

    public static void entityTick(Entity entity, Level level, RandomSource random) {
        EntityType<?> type = entity.getType();
        if (type.equals(EntityType.ENDER_PEARL) && ModConfigs.INSTANCE.enderPearlTrail.get()) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.PORTAL, entity.getRandomX(2), entity.getRandomY(), entity.getRandomZ(2), 0, 0, 0);
            }
        }
        else if (type.equals(EntityType.SNOWBALL) && ModConfigs.INSTANCE.snowballTrail.get()) {
            level.addParticle(ModParticles.SNOWBALL_TRAIL.get(), entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), 0, 0, 0);
        }
        else if (type.equals(EntityType.ALLAY)) {
            if (Math.min(random.nextFloat(), 1) < ModConfigs.INSTANCE.allayMagicChance.get()) {
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

    public static void entityHurt(LivingEntity entity, Level level, RandomSource random) {
        EntityType<?> type = entity.getType();
        if (type.equals(EntityType.CHICKEN) && ModConfigs.INSTANCE.chickenHitFeathers.get()) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ModParticles.CHICKEN_FEATHER.get(), entity.getX(), entity.getY(), entity.getZ(), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1));
            }
        }
        else if (type.equals(EntityType.PARROT) && ModConfigs.INSTANCE.parrotHitFeathers.get()) {
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

    // Only works for blocks that don't already implement animateTick
    public static void blockAnimateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.is(Blocks.REDSTONE_BLOCK) && ModConfigs.INSTANCE.redstoneBlockDust.get()) {
            Util.spawnParticlesAroundBlock(DustParticleOptions.REDSTONE, level, pos, random);
        }
        else if (state.is(Blocks.GLOWSTONE) && ModConfigs.INSTANCE.glowstoneBlockDust.get()) {
            Util.spawnParticlesAroundBlock(Util.GLOWSTONE_DUST_PARTICLES, level, pos, random);
        }
    }
}
