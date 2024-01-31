package einstein.ambient_sleep.networking.clientbound;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import einstein.ambient_sleep.AmbientSleep;
import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.util.ParticleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ClientBoundEntityFellPacket {

    public static final ResourceLocation CHANNEL = AmbientSleep.loc("entity_fell");

    private final int entityId;
    private final int fallDamage;
    private final double y;

    public ClientBoundEntityFellPacket(int entityId, int fallDamage, double y) {
        this.entityId = entityId;
        this.fallDamage = fallDamage;
        this.y = y;
    }

    public static ClientBoundEntityFellPacket decode(FriendlyByteBuf buf) {
        return new ClientBoundEntityFellPacket(buf.readVarInt(), buf.readInt(), buf.readDouble());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeInt(fallDamage);
        buf.writeDouble(y);
    }

    public static void handle(PacketContext<ClientBoundEntityFellPacket> context) {
        Level level = Minecraft.getInstance().level;
        if (context.side().equals(Side.CLIENT) && level != null) {
            ClientBoundEntityFellPacket packet = context.message();
            if (ModConfigs.INSTANCE.enableSleepingZs.get() && ModConfigs.INSTANCE.beehivesHaveSleepingZs.get()) {
                Entity entity = level.getEntity(packet.entityId);
                if (entity instanceof LivingEntity livingEntity) {
                    ParticleManager.entityFell(livingEntity, packet.y, packet.fallDamage);
                }
            }
        }
    }
}
