package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.configs.ModEntityConfigs;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static einstein.subtle_effects.init.ModConfigs.ENTITIES;
import static einstein.subtle_effects.util.MathUtil.*;

public class ClientPacketHandlers {

    public static void handle(ClientLevel level, ClientBoundEntityFellPayload payload) {
        if (level.getEntity(payload.entityId()) instanceof LivingEntity livingEntity) {
            ParticleSpawnUtil.spawnEntityFellParticles(livingEntity, payload.y(), payload.distance(), payload.fallDamage(), getEntityFellConfig(payload));
        }
    }

    public static void handle(ClientLevel level, ClientBoundEntitySpawnSprintingDustCloudsPayload payload) {
        if (level.getEntity(payload.entityId()) instanceof LivingEntity livingEntity) {
            int ySpeedModifier = 5;
            if (livingEntity instanceof Ravager) {
                ySpeedModifier = 20;
            }

            ParticleSpawnUtil.spawnCreatureMovementDustClouds(livingEntity, level, livingEntity.getRandom(), ySpeedModifier);
        }
    }

    public static void handle(ClientLevel level, ClientBoundSpawnSnoreParticlePayload payload) {
        if (ModConfigs.BLOCKS.beehivesHaveSleepingZs) {
            level.addParticle(ModParticles.SNORING.get(), payload.x(), payload.y(), payload.z(), 0, 0, 0);
        }
    }

    public static void handle(ClientLevel level, ClientBoundBlockDestroyEffectsPayload payload) {
        if (getBlockDestroyEffectConfig(payload)) {
            BlockPos pos = payload.pos();
            BlockState state = Block.stateById(payload.stateId());
            SoundType soundType = state.getSoundType();

            level.addDestroyBlockEffect(pos, state);
            level.playLocalSound(pos, soundType.getBreakSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * 0.8F, false);
        }
    }

    public static void handle(ClientLevel level, ClientBoundXPBottleEffectsPayload payload) {
        BlockPos pos = payload.pos();
        Vec3 vec3 = Vec3.atBottomCenterOf(pos);
        RandomSource random = level.getRandom();
        if (ENTITIES.xpBottleParticlesDisplayType == ModEntityConfigs.XPBottleParticlesDisplayType.BOTH || ENTITIES.xpBottleParticlesDisplayType == ModEntityConfigs.XPBottleParticlesDisplayType.VANILLA) {
            level.levelEvent(
                    LevelEvent.PARTICLES_SPELL_POTION_SPLASH,
                    pos,
                    PotionContents.getColor(Potions.WATER)
            );
        }

        if (ENTITIES.xpBottleParticlesDisplayType == ModEntityConfigs.XPBottleParticlesDisplayType.BOTH || ENTITIES.xpBottleParticlesDisplayType == ModEntityConfigs.XPBottleParticlesDisplayType.DEFAULT) {
            for (int i = 0; i < ENTITIES.xpBottleParticlesDensity.get(); ++i) {
                double d = random.nextDouble() * 4;
                double d1 = random.nextDouble() * Math.PI * 2;
                double xPower = Math.cos(d1) * d;
                double zPower = Math.sin(d1) * d;

                level.addParticle(
                        new FloatParticleOptions(ModParticles.EXPERIENCE.get(), (float) d),
                        vec3.x + xPower * 0.1,
                        vec3.y + 0.3,
                        vec3.z + zPower * 0.1,
                        xPower,
                        0.01 + random.nextDouble() * 0.5,
                        zPower
                );
            }
        }

        if (ENTITIES.xpBottleParticlesDisplayType == ModEntityConfigs.XPBottleParticlesDisplayType.DEFAULT) {
            for (int i = 0; i < 8; ++i) {
                level.addParticle(
                        new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SPLASH_POTION)),
                        vec3.x, vec3.y, vec3.z,
                        random.nextGaussian() * 0.15,
                        random.nextDouble() * 0.2,
                        random.nextGaussian() * 0.15
                );
            }

            level.playLocalSound(pos,
                    SoundEvents.SPLASH_POTION_BREAK,
                    SoundSource.NEUTRAL,
                    1,
                    random.nextFloat() * 0.1F + 0.9F,
                    false
            );
        }
    }

    public static void handle(ClientLevel level, ClientBoundFallingBlockLandPayload payload) {
        BlockPos pos = payload.pos();
        BlockState state = Block.stateById(payload.stateId());
        Block block = state.getBlock();

        if (BLOCKS.fallingBlocks.landSound && !(block instanceof AnvilBlock)) {
            SoundType soundType = state.getSoundType();

            Util.playClientSound(pos, soundType.getPlaceSound(), SoundSource.BLOCKS,
                    (soundType.getVolume() + 1.0F) / 2F,
                    soundType.getPitch() * 0.8F
            );
        }

        if (BLOCKS.fallingBlocks.landDust) {
            if (block instanceof FallingBlock fallingBlock) {
                RandomSource random = level.getRandom();
                int color = fallingBlock.getDustColor(state, level, pos);
                DustParticleOptions options = new DustParticleOptions(
                        new Vector3f(
                                ((color >> 16) & 255) / 255F,
                                ((color >> 8) & 255) / 255F,
                                (color & 255) / 255F
                        ), 1);

                for (int i = 0; i < 25; i++) {
                    boolean b = random.nextBoolean();
                    int xSign = nextSign(random);
                    int zSign = nextSign(random);

                    level.addParticle(options,
                            pos.getX() + 0.5 + (b ? 0.55 * xSign : nextNonAbsDouble(random, 0.55)),
                            pos.getY() + nextDouble(random, 0.3),
                            pos.getZ() + 0.5 + (!b ? 0.55 * zSign : nextNonAbsDouble(random, 0.55)),
                            b ? 50 * xSign : 0, // 50 because dust velocity gets multiplied by 0.1
                            0.3,
                            !b ? 50 * zSign : 0
                    );
                }
            }
        }
    }

    // Don't convert to enum parameters, because the server will crash trying to access the client configs
    private static boolean getBlockDestroyEffectConfig(ClientBoundBlockDestroyEffectsPayload packet) {
        return switch (packet.config()) {
            case LEAVES_DECAY -> ModConfigs.BLOCKS.leavesDecayEffects;
            case FARMLAND_DESTROY -> ModConfigs.BLOCKS.farmlandDestroyEffects;
        };
    }

    private static boolean getEntityFellConfig(ClientBoundEntityFellPayload packet) {
        return switch (packet.config()) {
            case ENTITY -> ENTITIES.dustClouds.mobFell;
            case PLAYER -> ENTITIES.dustClouds.playerFell;
            case MACE -> ENTITIES.dustClouds.landMaceAttack;
            case ELYTRA -> ENTITIES.dustClouds.flyIntoWall;
        };
    }
}
