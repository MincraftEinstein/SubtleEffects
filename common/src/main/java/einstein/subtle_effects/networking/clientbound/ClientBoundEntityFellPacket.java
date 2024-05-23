package einstein.subtle_effects.networking.clientbound;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ClientBoundEntityFellPacket {

    public static final ResourceLocation CHANNEL = SubtleEffects.loc("entity_fell");

    private final int entityId;
    private final float distance;
    private final double y;
    private final int fallDamage;

    public ClientBoundEntityFellPacket(int entityId, double y, float distance, int fallDamage) {
        this.entityId = entityId;
        this.distance = distance;
        this.y = y;
        this.fallDamage = fallDamage;
    }

    public static ClientBoundEntityFellPacket decode(FriendlyByteBuf buf) {
        return new ClientBoundEntityFellPacket(buf.readVarInt(), buf.readDouble(), buf.readFloat(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeDouble(y);
        buf.writeFloat(distance);
        buf.writeInt(fallDamage);
    }

    public static void handle(PacketContext<ClientBoundEntityFellPacket> context) {
        Level level = Minecraft.getInstance().level;
        if (context.side().equals(Side.CLIENT) && level != null) {
            ClientBoundEntityFellPacket packet = context.message();
            if (level.getEntity(packet.entityId) instanceof LivingEntity livingEntity) {
                ParticleSpawnUtil.spawnEntityFellParticles(livingEntity, packet.y, packet.distance, packet.fallDamage);
            }
        }
    }
}
