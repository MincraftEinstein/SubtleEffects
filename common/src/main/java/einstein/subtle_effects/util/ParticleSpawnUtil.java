package einstein.subtle_effects.util;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntityFellPacket;
import einstein.subtle_effects.particle.option.DirectionParticleOptions;
import einstein.subtle_effects.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static einstein.subtle_effects.init.ModConfigs.ENTITIES;
import static einstein.subtle_effects.util.MathUtil.*;
import static net.minecraft.util.Mth.DEG_TO_RAD;

public class ParticleSpawnUtil {

    public static void spawnSparks(Level level, RandomSource random, BlockPos pos, Vec3 offset, Vec3 maxSpeeds, int count, int size, boolean isSoulFlame, boolean longLife) {
        spawnSparks(level, random, pos, offset, maxSpeeds, count, size, size, isSoulFlame, longLife);
    }

    public static void spawnSparks(Level level, RandomSource random, BlockPos pos, Vec3 offset, Vec3 maxSpeeds, int count, int xSize, int zSize, boolean isSoulFlame, boolean longLife) {
        if (random.nextInt(1) == 0) {
            for (int i = 0; i < count; i++) {
                SimpleParticleType type = (isSoulFlame ? (longLife ? ModParticles.LONG_SOUL_SPARK : ModParticles.SHORT_SOUL_SPARK) : (longLife ? ModParticles.LONG_SPARK : ModParticles.SHORT_SPARK)).get();
                level.addParticle(type,
                        pos.getX() + offset.x() + random.nextDouble() / xSize * nextSign(random),
                        pos.getY() + offset.y(),
                        pos.getZ() + offset.z() + random.nextDouble() / zSize * nextSign(random),
                        nextNonAbsDouble(random, maxSpeeds.x()),
                        nextNonAbsDouble(random, maxSpeeds.y()),
                        nextNonAbsDouble(random, maxSpeeds.z())
                );
            }
        }
    }

    public static void spawnParticlesAroundBlock(ParticleOptions particle, Level level, BlockPos pos, RandomSource random, int perSideChance) {
        spawnParticlesAroundBlock(particle, level, pos, random, 0.0625F, perSideChance > 0 ? direction -> random.nextInt(perSideChance) != 0 : null);
    }

    public static void spawnParticlesAroundBlock(ParticleOptions particle, Level level, BlockPos pos, RandomSource random, float offset, @Nullable Predicate<Direction> predicate) {
        for (Direction direction : Direction.values()) {
            if (predicate != null && predicate.test(direction)) {
                return;
            }

            BlockPos relativePos = pos.relative(direction);
            if (!level.getBlockState(relativePos).isSolidRender(level, relativePos)) {
                spawnParticlesOnSide(particle, offset, direction, level, pos, random, 0, 0, 0);
            }
        }
    }

    public static void spawnParticlesOnSide(ParticleOptions particle, float offset, Direction direction, Level level, BlockPos pos, RandomSource random, double xSpeed, double ySpeed, double zSpeed) {
        double offsetFromCenter = 0.5 + offset;
        Direction.Axis axis = direction.getAxis();
        double xOffset = axis == Direction.Axis.X ? 0.5 + offsetFromCenter * direction.getStepX() : random.nextFloat();
        double yOffset = axis == Direction.Axis.Y ? 0.5 + offsetFromCenter * direction.getStepY() : random.nextFloat();
        double zOffset = axis == Direction.Axis.Z ? 0.5 + offsetFromCenter * direction.getStepZ() : random.nextFloat();
        level.addParticle(particle, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, xSpeed, ySpeed, zSpeed);
    }

    public static void spawnFallDustClouds(LivingEntity entity, float distance, int fallDamage) {
        Level level = entity.level();
        if (level.isClientSide && entity.equals(Minecraft.getInstance().player)) {
            spawnEntityFellParticles(entity, entity.getY(), distance, fallDamage, ENTITIES.dustClouds.playerFell);
        }
        else if (level instanceof ServerLevel serverLevel) {
            Services.NETWORK.sendToClientsTracking(
                    entity instanceof ServerPlayer player ? player : null,
                    serverLevel, entity.blockPosition(),
                    new ClientBoundEntityFellPacket(entity.getId(), entity.getY(), distance, fallDamage)
            );
        }
    }

    public static void spawnCreatureMovementDustClouds(LivingEntity entity, Level level, RandomSource random, int YSpeedModifier) {
        if (ModConfigs.ENTITIES.dustClouds.mobRunning) {
            spawnCreatureMovementDustCloudsNoConfig(entity, level, random, YSpeedModifier);
        }
    }

    public static void spawnCreatureMovementDustCloudsNoConfig(LivingEntity entity, Level level, RandomSource random, int YSpeedModifier) {
        if (ENTITIES.dustClouds.preventWhenRaining && level.isRainingAt(entity.blockPosition())) {
            return;
        }

        level.addParticle(ModParticles.LARGE_DUST_CLOUD.get(),
                entity.position().x + entity.getBbWidth() * random.nextDouble() - 1,
                entity.getY() + Math.max(Math.min(random.nextFloat(), 0.5), 0.2),
                entity.position().z + entity.getBbWidth() * random.nextDouble() - 1,
                0,
                random.nextDouble() * YSpeedModifier,
                0
        );
    }

    public static void spawnLavaSparks(Level level, BlockPos pos, RandomSource random, int count) {
        for (int i = 0; i < count; i++) {
            level.addParticle(ModParticles.FLOATING_SPARK.get(),
                    pos.getX() + 0.5 + random.nextDouble() / 2 * nextSign(random),
                    pos.getY() + random.nextDouble() * random.nextInt(3),
                    pos.getZ() + 0.5 + random.nextDouble() / 2 * nextSign(random),
                    nextNonAbsDouble(random, 0.1),
                    nextNonAbsDouble(random, 0.07),
                    nextNonAbsDouble(random, 0.1)
            );
        }
    }

    public static void spawnCmdBlockParticles(Level level, Vec3 pos, RandomSource random, BiPredicate<Direction, Vec3> directionValidator) {
        for (Direction direction : Direction.values()) {
            Vec3 endPos = pos.relative(direction, 1);
            Vec3 relativePos = endPos.relative(direction, -0.5);

            if (directionValidator.test(direction, endPos)) {
                Vec3 speed = pos.vectorTo(relativePos).offsetRandom(random, 1);
                level.addParticle(new DirectionParticleOptions(ModParticles.COMMAND_BLOCK.get(), direction), endPos.x(), endPos.y(), endPos.z(), speed.x(), speed.y(), speed.z());
            }
        }
    }

    public static void spawnHeatedWaterParticles(Level level, BlockPos pos, RandomSource random, boolean isFalling, double height, boolean steamConfig, boolean boilingConfig) {
        int brightness = level.getBrightness(LightLayer.BLOCK, pos);

        switch (BLOCKS.steam.spawnLogic) {
            case NEAR_LAVA -> {
                for (int x = -1; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        for (int z = -1; z < 2; z++) {
                            if (level.getFluidState(pos.offset(x, y, z)).is(FluidTags.LAVA)) {
                                spawnHeatedWaterParticles(level, pos, random, isFalling, height, steamConfig, boilingConfig, brightness);
                            }
                        }
                    }
                }
            }
            case BRIGHTNESS -> {
                if (brightness > BLOCKS.steam.steamingThreshold.get() || level.getBlockState(pos.below()).is(Blocks.MAGMA_BLOCK)) {
                    spawnHeatedWaterParticles(level, pos, random, isFalling, height, steamConfig, boilingConfig, brightness);
                }
            }
        }
    }

    private static void spawnHeatedWaterParticles(Level level, BlockPos pos, RandomSource random, boolean isFalling, double height, boolean steamConfig, boolean boilingConfig, int brightness) {
        if (steamConfig) {
            if (!isFalling && !Util.isSolidOrNotEmpty(level, pos.above())) {
                level.addParticle(ModParticles.STEAM.get(),
                        pos.getX() + random.nextDouble(),
                        pos.getY() + 0.875 + nextDouble(random, 0.5),
                        pos.getZ() + random.nextDouble(),
                        0, 0, 0
                );
            }
        }

        if (boilingConfig && brightness >= BLOCKS.steam.boilingThreshold.get()) {
            level.addParticle(ParticleTypes.BUBBLE,
                    pos.getX() + random.nextDouble(),
                    Mth.clamp(random.nextDouble(), pos.getY(), pos.getY() + height),
                    pos.getZ() + random.nextDouble(),
                    0, 0, 0
            );
        }
    }

    public static void spawnEntityFellParticles(LivingEntity entity, double y, float distance, int fallDamage, boolean config) {
        if (!config || entity.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
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

        if (ENTITIES.dustClouds.preventWhenRaining && level.isRainingAt(entity.blockPosition())) {
            return;
        }

        if (!level.getFluidState(entity.getOnPos().atY(Mth.floor(y))).isEmpty()) {
            return;
        }

        if (fallDamage < 4) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(ModParticles.SMALL_DUST_CLOUD.get(),
                        entity.getRandomX(1),
                        y + Math.max(Math.min(random.nextFloat(), 0.5), 0.2),
                        entity.getRandomZ(1),
                        0.3 * nextSign(random),
                        random.nextDouble(),
                        0.3 * nextSign(random)
                );
            }
            return;
        }

        for (int i = 0; i < 10; i++) {
            level.addParticle(ModParticles.LARGE_DUST_CLOUD.get(),
                    entity.getRandomX(1),
                    y + Math.max(Math.min(random.nextFloat(), 0.5), 0.2),
                    entity.getRandomZ(1),
                    0.5 * nextSign(random),
                    random.nextDouble() * 3,
                    0.5 * nextSign(random)
            );
        }
    }

    // Should be roughly the same as LivingEntity.spawnItemParticles()
    public static void spawnEntityFaceParticle(ParticleOptions options, LivingEntity entity, Level level, RandomSource random, Vec3 offset, float partialTick) {
        spawnEntityFaceParticle(options, entity, level, random,
                new Vec3((random.nextFloat() - 0.5) * 0.3, -random.nextFloat() * 0.6 - 0.3, 0.6).add(offset),
                new Vec3((random.nextFloat() - 0.5) * 0.1, (Math.random() * 0.1 + 0.1) + 0.05, 0), partialTick
        );
    }

    public static void spawnEntityFaceParticle(ParticleOptions options, LivingEntity entity, Level level, RandomSource random, Vec3 offset, Vec3 speed, float partialTick) {
        speed = speed.xRot(-entity.getViewXRot(partialTick) * DEG_TO_RAD);
        speed = speed.yRot(-entity.getViewYRot(partialTick) * DEG_TO_RAD);
        spawnEntityFaceParticle(options, entity, level, offset, speed, partialTick);
    }

    public static void spawnEntityFaceParticle(ParticleOptions options, LivingEntity entity, Level level, Vec3 offset, Vec3 speed, float partialTick) {
        spawnEntityHeadParticle(options, entity, level, offset.add(0, 0, 0.6), speed, partialTick);
    }

    public static void spawnEntityHeadParticle(ParticleOptions options, LivingEntity entity, Level level, Vec3 pos, Vec3 speed, float partialTick) {
        pos = pos.xRot(-entity.getViewXRot(partialTick) * DEG_TO_RAD);
        pos = pos.yRot(-entity.getViewYRot(partialTick) * DEG_TO_RAD);
        pos = pos.add(entity.getX(), entity.getEyeY(), entity.getZ());
        level.addParticle(options, pos.x(), pos.y(), pos.z(), speed.x(), speed.y(), speed.z());
    }
}
