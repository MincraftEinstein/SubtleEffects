package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class ClientPacketHandlers {

    public static void handle(ClientLevel level, ClientBoundEntityFellPacket packet) {
        if (level.getEntity(packet.entityId()) instanceof LivingEntity livingEntity) {
            ParticleSpawnUtil.spawnEntityFellParticles(livingEntity, packet.y(), packet.distance(), packet.fallDamage());
        }
    }

    public static void handle(ClientLevel level, ClientBoundEntitySpawnSprintingDustCloudsPacket packet) {
        if (level.getEntity(packet.entityId()) instanceof LivingEntity livingEntity) {
            int ySpeedModifier = 5;
            if (livingEntity instanceof Ravager) {
                ySpeedModifier = 20;
            }

            ParticleSpawnUtil.spawnCreatureMovementDustClouds(livingEntity, level, livingEntity.getRandom(), ySpeedModifier);
        }
    }

    public static void handle(ClientLevel level, ClientBoundSpawnSnoreParticlePacket packet) {
        if (ModConfigs.BLOCKS.beehivesHaveSleepingZs) {
            level.addParticle(ModParticles.SNORING.get(), packet.x(), packet.y(), packet.z(), 0, 0, 0);
        }
    }

    public static void handle(ClientLevel level, ClientBoundBlockDestroyEffectsPacket packet) {
        if (getBlockDestroyEffectConfig(packet)) {
            BlockPos pos = packet.pos();
            BlockState state = Block.stateById(packet.stateId());
            SoundType soundType = state.getSoundType();

            level.addDestroyBlockEffect(pos, state);
            level.playLocalSound(pos, soundType.getBreakSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * 0.8F, false);
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
