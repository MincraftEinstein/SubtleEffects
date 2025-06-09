package einstein.subtle_effects.util;

import einstein.subtle_effects.configs.ModBlockConfigs;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.mixin.client.item.BucketItemAccessor;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntityFellPacket;
import einstein.subtle_effects.particle.EnderEyePlacedRingParticle;
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.particle.option.ColorParticleOptions;
import einstein.subtle_effects.particle.option.DirectionParticleOptions;
import einstein.subtle_effects.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static einstein.subtle_effects.init.ModConfigs.ENTITIES;
import static einstein.subtle_effects.util.MathUtil.*;
import static net.minecraft.util.Mth.DEG_TO_RAD;
import static net.minecraft.util.Mth.nextFloat;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF;

public class ParticleSpawnUtil {

    public static void spawnSparks(Level level, RandomSource random, BlockPos pos, SparkType sparkType, Box box, Vec3 maxSpeeds, int count, List<Integer> colors) {
        if (random.nextBoolean()) {
            Vec3 start = box.min();
            Vec3 end = box.max();

            for (int i = 0; i < count; i++) {
                level.addParticle(SparkParticle.create(sparkType, random, colors),
                        pos.getX() + Mth.nextDouble(random, start.x, end.x),
                        pos.getY() + Mth.nextDouble(random, start.y, end.y),
                        pos.getZ() + Mth.nextDouble(random, start.z, end.z),
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

    public static void spawnFallDustClouds(LivingEntity entity, float distance, int fallDamage, ClientBoundEntityFellPacket.TypeConfig config) {
        Level level = entity.level();
        if (level.isClientSide && entity.equals(Minecraft.getInstance().player)) {
            spawnEntityFellParticles(entity, entity.getY(), distance, fallDamage, ENTITIES.dustClouds.playerFell);
        }
        else if (level instanceof ServerLevel serverLevel) {
            Services.NETWORK.sendToClientsTracking(
                    entity instanceof ServerPlayer player ? player : null,
                    serverLevel, entity.blockPosition(),
                    new ClientBoundEntityFellPacket(entity.getId(), entity.getY(), distance, fallDamage, config)
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

        if (entity.isInvisible()) {
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
        height -= 0.1;

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

        if (entity.isInvisible()) {
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

    public static void spawnEnderEyePlacementParticles(BlockPos pos, RandomSource random, Level level, int color) {
        if (BLOCKS.enderEyePlacedRings) {
            level.addParticle(new ColorParticleOptions(ModParticles.ENDER_EYE_PLACED_RING.get(), Vec3.fromRGB24(color).toVector3f()),
                    pos.getX() + 0.5, pos.getY() + 0.8125 + EnderEyePlacedRingParticle.SIZE, pos.getZ() + 0.5,
                    0, 0, 0
            );
        }

        if (BLOCKS.enderEyePlacedParticlesDisplayType != ModBlockConfigs.EnderEyePlacedParticlesDisplayType.VANILLA) {
            spawnEndPortalParticles(level, pos, random, new ColorParticleOptions(ModParticles.SHORT_SPARK.get(), Vec3.fromRGB24(color).toVector3f()), 16);
        }
    }

    public static void spawnEndPortalParticles(Level level, BlockPos pos, RandomSource random, ParticleOptions particle, int count) {
        for (int i = 0; i < count; ++i) {
            level.addParticle(particle,
                    pos.getX() + 0.5 + nextNonAbsDouble(random, 0.25),
                    pos.getY() + 0.9375,
                    pos.getZ() + 0.5 + nextNonAbsDouble(random, 0.25),
                    0, 0, 0
            );
        }
    }

    public static void spawnParticlesAroundShape(ParticleOptions particle, Level level, BlockPos pos, BlockState state, int count, Supplier<Vec3> particleSpeed, float offset) {
        if (state.hasProperty(DOUBLE_BLOCK_HALF)) {
            DoubleBlockHalf half = state.getValue(DOUBLE_BLOCK_HALF);
            BlockPos oppositePos = pos.relative(half == DoubleBlockHalf.UPPER ? Direction.UP : Direction.DOWN);
            BlockState oppositeState = level.getBlockState(oppositePos);

            if (oppositeState.is(state.getBlock()) && oppositeState.hasProperty(DOUBLE_BLOCK_HALF)) {
                DoubleBlockHalf otherHalf = half == DoubleBlockHalf.UPPER ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER;
                if (otherHalf.equals(oppositeState.getValue(DOUBLE_BLOCK_HALF))) {
                    spawnParticlesAroundShape(particle, level, oppositePos, state,
                            direction ->
                                    (half == DoubleBlockHalf.LOWER && direction == Direction.UP)
                                            || (half == DoubleBlockHalf.UPPER && direction == Direction.DOWN),
                            count, particleSpeed, offset
                    );
                }
            }
        }

        spawnParticlesAroundShape(particle, level, pos, state, null, count, particleSpeed, offset);
    }

    public static void spawnParticlesAroundShape(ParticleOptions particle, Level level, BlockPos pos, BlockState state, @Nullable Predicate<Direction> predicate, int count, Supplier<Vec3> particleSpeed, float offset) {
        RandomSource random = level.getRandom();

        state.getShape(level, pos).forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            for (Direction direction : Direction.values()) {
                if (predicate != null && predicate.test(direction)) {
                    continue;
                }

                BlockPos relativePos = pos.relative(direction);
                if (level.getBlockState(relativePos).isSolidRender(level, relativePos)) {
                    continue;
                }

                Direction.Axis axis = direction.getAxis();
                Direction.AxisDirection axisDirection = direction.getAxisDirection();
                boolean isPositive = axisDirection == Direction.AxisDirection.POSITIVE;

                for (int i = 0; i < count; i++) {
                    double xOffset = axis == Direction.Axis.X ? (isPositive ? maxX : minX) : Mth.nextDouble(random, minX, maxX);
                    double yOffset = axis == Direction.Axis.Y ? (isPositive ? maxY : minY) : Mth.nextDouble(random, minY, maxY);
                    double zOffset = axis == Direction.Axis.Z ? (isPositive ? maxZ : minZ) : Mth.nextDouble(random, minZ, maxZ);
                    Vec3 speed = particleSpeed.get();

                    level.addParticle(particle,
                            pos.getX() + xOffset + (offset * axisDirection.getStep()),
                            pos.getY() + yOffset + (offset * axisDirection.getStep()),
                            pos.getZ() + zOffset + (offset * axisDirection.getStep()),
                            speed.x(), speed.y(), speed.z()
                    );
                }
            }
        });
    }

    public static void spawnHammeringWorkstationParticles(BlockPos pos, RandomSource random, Level level) {
        float pointX = random.nextFloat();
        float pointZ = random.nextFloat();

        for (int i2 = 0; i2 < 20; i2++) {
            int xSign = nextSign(random);
            int zSign = nextSign(random);

            level.addParticle(SparkParticle.create(SparkType.METAL, random),
                    pos.getX() + pointX,
                    pos.getY() + 1,
                    pos.getZ() + pointZ,
                    nextFloat(random, 0.1F, 0.2F) * xSign,
                    nextFloat(random, 0.1F, 0.2F),
                    nextFloat(random, 0.1F, 0.2F) * zSign
            );
        }
    }

    public static void spawnCompostParticles(Level level, BlockPos pos, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed) {
        RandomSource random = level.getRandom();
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof ComposterBlock) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(particle,
                        pos.getX() + 0.5 + MathUtil.nextNonAbsDouble(random, 0.3),
                        pos.getY() + 0.1875 + (0.125 * state.getValue(ComposterBlock.LEVEL)),
                        pos.getZ() + 0.5 + MathUtil.nextNonAbsDouble(random, 0.3),
                        xSpeed, ySpeed, zSpeed);
            }
        }
    }

    public static void spawnBucketParticles(Level level, BlockPos pos, ItemStack stack) {
        if (level.isClientSide) {
            if (stack.getItem() instanceof BucketItemAccessor bucket) {
                Fluid content = bucket.getContent();
                boolean isWater = content.isSame(Fluids.WATER);

                if ((isWater && ModConfigs.ITEMS.waterBucketUseParticles) || (content.isSame(Fluids.LAVA) && ModConfigs.ITEMS.lavaBucketUseParticles)) {
                    if (isWater && level.dimensionType().ultraWarm()) {
                        return;
                    }

                    spawnBucketParticles(level, pos, Util.getParticleForFluid(content));
                }
            }
            else if (stack.is(Items.POWDER_SNOW_BUCKET) && ModConfigs.ITEMS.powderSnowBucketUseParticles) {
                spawnBucketParticles(level, pos, ModParticles.SNOW.get());
            }
        }
    }

    public static void spawnBucketParticles(Level level, BlockPos pos, ParticleOptions particle) {
        if (particle != null) {
            RandomSource random = level.getRandom();
            FluidState fluidState = level.getFluidState(pos);
            double fluidHeight = fluidState.getHeight(level, pos);

            for (int i = 0; i < 16; i++) {
                level.addParticle(particle,
                        pos.getX() + 0.5 + nextNonAbsDouble(random),
                        pos.getY() + (fluidHeight == 0 ? random.nextDouble() : fluidHeight),
                        pos.getZ() + 0.5 + nextNonAbsDouble(random),
                        0, 0, 0
                );
            }
        }
    }

    public static void spawnGrindstoneUsedParticles(Level level, BlockPos pos, BlockState state, RandomSource random) {
        if (BLOCKS.grindstoneUseParticles) {
            if (state.hasProperty(GrindstoneBlock.FACING) && state.hasProperty(GrindstoneBlock.FACE)) {
                Direction direction = state.getValue(GrindstoneBlock.FACING);
                AttachFace face = state.getValue(GrindstoneBlock.FACE);
                Direction side = face == AttachFace.CEILING ? Direction.DOWN : Direction.UP;

                for (int i = 0; i < 20; i++) {
                    spawnParticlesOnSide(SparkParticle.create(SparkType.METAL, random), 0, side, level, pos, random,
                            nextFloat(random, 0.1F, 0.2F) * (direction.getStepX() * 1.5),
                            face == AttachFace.CEILING ? 0 : nextFloat(random, 0.1F, 0.2F),
                            nextFloat(random, 0.1F, 0.2F) * (direction.getStepZ() * 1.5)
                    );
                }
            }
        }
    }

    public static void spawnStonecutterParticles(ClientLevel level, ItemStack stack, BlockPos pos, BlockState state) {
        if (!BLOCKS.stonecutterUseParticles) {
            return;
        }

        RandomSource random = level.getRandom();

        if (state.hasProperty(StonecutterBlock.FACING)) {
            Direction direction = state.getValue(StonecutterBlock.FACING).getClockWise();

            for (int i = 0; i < 16; i++) {
                spawnParticlesOnSide(new ItemParticleOption(ParticleTypes.ITEM, stack),
                        -0.125F, Direction.UP, level, pos, random,
                        nextFloat(random, 0.1F, 0.2F) * (direction.getStepX() * 1.5),
                        nextFloat(random, 0.1F, 0.2F),
                        nextFloat(random, 0.1F, 0.2F) * (direction.getStepZ() * 1.5)
                );
            }
        }
    }
}
