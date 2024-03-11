package einstein.ambient_sleep.util;

import commonnetwork.api.Dispatcher;
import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.networking.clientbound.ClientBoundEntityFellPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static einstein.ambient_sleep.init.ModConfigs.INSTANCE;
import static einstein.ambient_sleep.util.MathUtil.nextFloat;
import static einstein.ambient_sleep.util.MathUtil.nextSign;

public class Util {

    public static final int BREATH_DELAY = 60;
    public static final int SNORE_DELAY = 10;
    public static final int MAX_Z_COUNT = 3;
    public static final int STOMACH_GROWL_DELAY = 300;
    public static final DustParticleOptions GLOWSTONE_DUST_PARTICLES = new DustParticleOptions(Vec3.fromRGB24(0xFFBC5E).toVector3f(), 1);

    public static void playClientSound(SoundSource source, Entity entity, SoundEvent sound, float volume, float pitch) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level.isClientSide) {
            level.playSeededSound(minecraft.player, entity, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), source, volume, pitch, level.random.nextLong());
        }
    }

    public static void spawnSparks(Level level, RandomSource random, BlockPos pos, Vec3 offset, Vec3i maxSpeeds, int count, int size, boolean isSoulFlame, boolean longLife) {
        spawnSparks(level, random, pos, offset, maxSpeeds, count, size, size, isSoulFlame, longLife);
    }

    public static void spawnSparks(Level level, RandomSource random, BlockPos pos, Vec3 offset, Vec3i maxSpeeds, int count, int xSize, int zSize, boolean isSoulFlame, boolean longLife) {
        if (random.nextInt(1) == 0) {
            for (int i = 0; i < count; i++) {
                SimpleParticleType type = (isSoulFlame ? (longLife ? ModParticles.LONG_SOUL_SPARK : ModParticles.SHORT_SOUL_SPARK) : (longLife ? ModParticles.LONG_SPARK : ModParticles.SHORT_SPARK)).get();
                level.addParticle(type,
                        pos.getX() + offset.x() + random.nextDouble() / xSize * nextSign(),
                        pos.getY() + offset.y(),
                        pos.getZ() + offset.z() + random.nextDouble() / zSize * nextSign(),
                        nextFloat(maxSpeeds.getX()) * nextSign(),
                        nextFloat(maxSpeeds.getY()) * nextSign(),
                        nextFloat(maxSpeeds.getZ()) * nextSign()
                );
            }
        }
    }

    public static void spawnParticlesAroundBlock(ParticleOptions particle, Level level, BlockPos pos, RandomSource random) {
        for (Direction direction : Direction.values()) {
            BlockPos relativePos = pos.relative(direction);
            if (!level.getBlockState(relativePos).isSolidRender(level, relativePos)) {
                spawnParticlesOnSide(particle, 0.0625F, direction, level, pos, random, 0, 0, 0);
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
            ParticleManager.entityFell(entity, entity.getY(), distance, fallDamage);
        }
        else if (!level.isClientSide && !entity.equals(Minecraft.getInstance().player)) {
            Dispatcher.sendToAllClients(new ClientBoundEntityFellPacket(entity.getId(), entity.getY(), distance, fallDamage), level.getServer());
        }
    }

    public static void spawnCreatureMovementDustClouds(LivingEntity entity, Level level, RandomSource random, int YSpeedModifier) {
        if (INSTANCE.mobSprintingDustClouds.get()) {
            level.addParticle(ModParticles.LARGE_DUST_CLOUD.get(),
                    entity.position().x + entity.getBbWidth() * random.nextDouble() - 1,
                    entity.getY() + Math.max(Math.min(random.nextFloat(), 0.5), 0.2),
                    entity.position().z + entity.getBbWidth() * random.nextDouble() - 1,
                    0,
                    random.nextDouble() * YSpeedModifier,
                    0
            );
        }
    }

    public static void spawnLavaSparks(Level level, BlockPos pos, RandomSource random, int count) {
        for (int i = 0; i < count; i++) {
            level.addParticle(ModParticles.FLOATING_SPARK.get(),
                    pos.getX() + 0.5 + random.nextDouble() / 2 * nextSign(),
                    pos.getY() + random.nextDouble() * random.nextInt(3),
                    pos.getZ() + 0.5 + random.nextDouble() / 2 * nextSign(),
                    nextFloat(10) * nextSign(),
                    nextFloat(7) ,
                    nextFloat(10) * nextSign()
            );
        }
    }

    public static boolean isSolidOrNotEmpty(Level level, BlockPos pos) {
        return level.getBlockState(pos).isSolidRender(level, pos) || !level.getFluidState(pos).isEmpty();
    }
}
