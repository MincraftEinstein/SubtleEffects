package einstein.subtle_effects.networking.clientbound;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public record ClientBoundEntityFellPacket(int entityId, double y, float distance,
                                          int fallDamage) implements CustomPacketPayload {

    public static final Type<ClientBoundEntityFellPacket> TYPE = new Type<>(SubtleEffects.loc("entity_fell"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundEntityFellPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundEntityFellPacket::entityId,
            ByteBufCodecs.DOUBLE, ClientBoundEntityFellPacket::y,
            ByteBufCodecs.FLOAT, ClientBoundEntityFellPacket::distance,
            ByteBufCodecs.INT, ClientBoundEntityFellPacket::fallDamage,
            ClientBoundEntityFellPacket::new
    );

    public static void handle(PacketContext<ClientBoundEntityFellPacket> context) {
        if (context.side().equals(Side.CLIENT)) {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                ClientBoundEntityFellPacket packet = context.message();
                if (level.getEntity(packet.entityId) instanceof LivingEntity livingEntity) {
                    ParticleSpawnUtil.spawnEntityFellParticles(livingEntity, packet.y, packet.distance, packet.fallDamage);
                }
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
