package einstein.ambient_sleep.networking.clientbound;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import einstein.ambient_sleep.AmbientSleep;
import einstein.ambient_sleep.util.ParticleSpawnUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.level.Level;

public class ClientBoundEntitySpawnSprintingDustCloudsPacket {

    public static final ResourceLocation CHANNEL = AmbientSleep.loc("entity_spawn_sprinting_dust_clouds");

    private final int entityId;

    public ClientBoundEntitySpawnSprintingDustCloudsPacket(int entityId) {
        this.entityId = entityId;
    }

    public static ClientBoundEntitySpawnSprintingDustCloudsPacket decode(FriendlyByteBuf buf) {
        return new ClientBoundEntitySpawnSprintingDustCloudsPacket(buf.readVarInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
    }

    public static void handle(PacketContext<ClientBoundEntitySpawnSprintingDustCloudsPacket> context) {
        Level level = Minecraft.getInstance().level;
        if (context.side().equals(Side.CLIENT) && level != null) {
            ClientBoundEntitySpawnSprintingDustCloudsPacket packet = context.message();
            if (level.getEntity(packet.entityId) instanceof LivingEntity livingEntity) {
                int ySpeedModifier = 5;
                if (livingEntity instanceof Ravager) {
                    ySpeedModifier = 20;
                }

                ParticleSpawnUtil.spawnCreatureMovementDustClouds(livingEntity, level, livingEntity.getRandom(), ySpeedModifier);
            }
        }
    }
}
