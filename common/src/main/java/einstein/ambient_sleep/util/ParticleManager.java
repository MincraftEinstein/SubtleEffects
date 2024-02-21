package einstein.ambient_sleep.util;

import einstein.ambient_sleep.AmbientSleep;
import einstein.ambient_sleep.init.BiomeParticles;
import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.init.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

import static einstein.ambient_sleep.init.ModConfigs.INSTANCE;

public class ParticleManager {

    private static final BlockPos.MutableBlockPos BIOME_POS = new BlockPos.MutableBlockPos();

    public static void entityTick(Entity entity, Level level, RandomSource random) {
        if (!level.isClientSide) {
            return;
        }

        float bbWidth = entity.getBbWidth();
        float bbHeight = entity.getBbHeight();

        if (bbWidth <= 4 && bbHeight <= 4 && entity.isOnFire()) {
            level.addParticle(ParticleTypes.SMOKE, entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), 0, 0, 0);

            if (bbWidth < 2 && bbHeight < 2 && !random.nextBoolean()) {
                return;
            }

            level.addParticle(ParticleTypes.FLAME, entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), 0, 0, 0);
        }

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
        else if (type.equals(EntityType.CAMEL)) {
            Camel camel = (Camel) entity;
            if (camel.isDashing() && camel.onGround()) {
                for (int i = 0; i < 10; i++) {
                    Util.spawnCreatureMovementDustClouds(camel, level, random, 5);
                }
            }
        }
        else if (entity instanceof Player player) {
            if (INSTANCE.sprintingDustClouds.get() && player.canSpawnSprintParticle() && player.onGround()) {
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
                Util.spawnCreatureMovementDustClouds(ravager, level, random, 20);
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
                level.addParticle(ModParticles.CHICKEN_FEATHER.get(), entity.getX(), entity.getY(0.5), entity.getZ(), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1));
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
                level.addParticle(particle, entity.getX(), entity.getY(0.5), entity.getZ(), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1));
            }
        }
        else if (type.equals(EntityType.SNOW_GOLEM) && INSTANCE.snowGolemHitSnowflakes.get()) {
            for (int i = 0; i < 20; i++) {
                level.addParticle(ModParticles.SNOWBALL_TRAIL.get(), entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), 0, 0, 0);
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
                        0.3 * (random.nextBoolean() ? 1 : -1),
                        random.nextDouble(),
                        0.3 * (random.nextBoolean() ? 1 : -1)
                );
            }
            return;
        }

        for (int i = 0; i < 10; i++) {
            level.addParticle(ModParticles.LARGE_DUST_CLOUD.get(),
                    entity.getRandomX(1),
                    y + Math.max(Math.min(random.nextFloat(), 0.5), 0.2),
                    entity.getRandomZ(1),
                    0.5 * (random.nextBoolean() ? 1 : -1),
                    random.nextDouble() * 3,
                    0.5 * (random.nextBoolean() ? 1 : -1)
            );
        }
    }

    // Only works for blocks that don't already implement animateTick
    public static void blockAnimateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.is(Blocks.REDSTONE_BLOCK) && INSTANCE.redstoneBlockDust.get()) {
            Util.spawnParticlesAroundBlock(DustParticleOptions.REDSTONE, level, pos, random);
        }
        else if (state.is(Blocks.GLOWSTONE)) {
            if (INSTANCE.glowstoneBlockDust.get().equals(ModConfigs.GlowstoneDustSpawnType.OFF)
                    || (INSTANCE.glowstoneBlockDust.get().equals(ModConfigs.GlowstoneDustSpawnType.NETHER_ONLY)
                    && !level.dimension().equals(Level.NETHER))) {
                return;
            }

            Util.spawnParticlesAroundBlock(Util.GLOWSTONE_DUST_PARTICLES, level, pos, random);
        }
    }

    public static void levelTickEnd(Minecraft minecraft, Level level) {
        Player player = minecraft.player;
        if (minecraft.isPaused() || minecraft.level == null || player == null) {
            return;
        }

        int radius = INSTANCE.biomeParticlesRadius.get();

        if (radius <= 0) {
            return;
        }

        for (int i = 0; i < 100; i++) {
            RandomSource random = level.getRandom();
            int x = player.getBlockX() + random.nextInt(radius) - random.nextInt(radius);
            int y = player.getBlockY() + random.nextInt(radius) - random.nextInt(radius);
            int z = player.getBlockZ() + random.nextInt(radius) - random.nextInt(radius);
            BIOME_POS.set(x, y, z);

            int surfaceLevel = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
            if (surfaceLevel > y) {
                continue;
            }

            Holder<Biome> biome = level.getBiome(BIOME_POS);
            for (BiomeParticles.BiomeParticleSettings settings : BiomeParticles.BIOME_PARTICLE_SETTINGS) {
                if (settings.density().get() > i && settings.spawnConditions().test(level)) {
                    List<Biome> biomes = settings.getBiomes(level);
                    if (biomes.isEmpty()) {
                        continue;
                    }

                    if (biomes.contains(biome.value())) {
                        if ((surfaceLevel + settings.maxSpawnHeight()) < y) {
                            continue;
                        }

                        BlockState state = level.getBlockState(BIOME_POS);
                        if (!state.isCollisionShapeFullBlock(level, BIOME_POS)) {
                            level.addParticle(settings.particle().get(), x + random.nextDouble(), y + random.nextDouble(), z + random.nextDouble(), 0, 0, 0);
                        }
                    }
                }
            }
        }
    }
}
