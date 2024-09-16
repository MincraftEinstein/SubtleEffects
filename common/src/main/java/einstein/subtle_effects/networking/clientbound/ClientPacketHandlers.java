package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientPacketHandlers {

    public static void handle(ClientBoundEntityFellPacket packet) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            if (level.getEntity(packet.entityId()) instanceof LivingEntity livingEntity) {
                ParticleSpawnUtil.spawnEntityFellParticles(livingEntity, packet.y(), packet.distance(), packet.fallDamage());
            }
        }
    }

    public static void handle(ClientBoundEntitySpawnSprintingDustCloudsPacket packet) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            if (level.getEntity(packet.entityId()) instanceof LivingEntity livingEntity) {
                int ySpeedModifier = 5;
                if (livingEntity instanceof Ravager) {
                    ySpeedModifier = 20;
                }

                ParticleSpawnUtil.spawnCreatureMovementDustClouds(livingEntity, level, livingEntity.getRandom(), ySpeedModifier);
            }
        }
    }

    public static void handle(ClientBoundSpawnSnoreParticlePacket packet) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            if (ModConfigs.INSTANCE.beehivesHaveSleepingZs.get()) {
                level.addParticle(ModParticles.SNORING.get(), packet.x(), packet.y(), packet.z(), 0, 0, 0);
            }
        }
    }

    public static void handle(ClientBoundBlockDestroyEffectsPacket packet) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            if (getConfig(packet).get()) {
                BlockPos pos = packet.pos();
                BlockState state = Block.stateById(packet.stateId());
                SoundType soundtype = state.getSoundType();

                level.addDestroyBlockEffect(pos, state);
                level.playLocalSound(pos, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1F) / 2F, soundtype.getPitch() * 0.8F, false);
            }
        }
    }

    // Don't convert to enum parameters, because the server will crash trying to access the client configs
    private static ModConfigSpec.BooleanValue getConfig(ClientBoundBlockDestroyEffectsPacket packet) {
        return switch (packet.config()) {
            case LEAVES_DECAY -> ModConfigs.INSTANCE.leavesDecayEffects;
            case FARMLAND_DESTROY -> ModConfigs.INSTANCE.farmlandDestroyEffects;
        };
    }
}
