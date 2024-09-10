package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.level.Level;

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
}
