package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.configs.ModEntityConfigs;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class ClientPacketHandlers {

    public static void handle(ClientBoundEntityFellPacket packet) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        if (level.getEntity(packet.entityId()) instanceof LivingEntity livingEntity) {
            ParticleSpawnUtil.spawnEntityFellParticles(livingEntity, packet.y(), packet.distance(), packet.fallDamage(), livingEntity instanceof Player ? ENTITIES.dustClouds.playerFell : ENTITIES.dustClouds.mobFell);
        }
    }

    public static void handle(ClientBoundEntitySpawnSprintingDustCloudsPacket packet) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        if (level.getEntity(packet.entityId()) instanceof LivingEntity livingEntity) {
            int ySpeedModifier = 5;
            if (livingEntity instanceof Ravager) {
                ySpeedModifier = 20;
            }

            ParticleSpawnUtil.spawnCreatureMovementDustClouds(livingEntity, level, livingEntity.getRandom(), ySpeedModifier);
        }
    }

    public static void handle(ClientBoundSpawnSnoreParticlePacket packet) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        if (ModConfigs.BLOCKS.beehivesHaveSleepingZs) {
            level.addParticle(ModParticles.SNORING.get(), packet.x(), packet.y(), packet.z(), 0, 0, 0);
        }
    }

    public static void handle(ClientBoundBlockDestroyEffectsPacket packet) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        if (getBlockDestroyEffectConfig(packet)) {
            BlockPos pos = packet.pos();
            BlockState state = Block.stateById(packet.stateId());
            SoundType soundType = state.getSoundType();

            level.addDestroyBlockEffect(pos, state);
            level.playLocalSound(pos, soundType.getBreakSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * 0.8F, false);
        }
    }

    public static void handle(ClientBoundXPBottleEffectsPacket packet) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        BlockPos pos = packet.pos();
        Vec3 vec3 = Vec3.atBottomCenterOf(pos);
        RandomSource random = level.getRandom();
        if (ENTITIES.xpBottleParticlesDisplayType == ModEntityConfigs.XPBottleParticlesDisplayType.BOTH || ENTITIES.xpBottleParticlesDisplayType == ModEntityConfigs.XPBottleParticlesDisplayType.VANILLA) {
            level.levelEvent(
                    LevelEvent.PARTICLES_SPELL_POTION_SPLASH,
                    pos,
                    PotionUtils.getColor(Potions.WATER)
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

    // Don't convert to enum parameters, because the server will crash trying to access the client configs
    private static boolean getBlockDestroyEffectConfig(ClientBoundBlockDestroyEffectsPacket packet) {
        return switch (packet.config()) {
            case LEAVES_DECAY -> ModConfigs.BLOCKS.leavesDecayEffects;
            case FARMLAND_DESTROY -> ModConfigs.BLOCKS.farmlandDestroyEffects;
        };
    }
}
