package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundEntityFellPacket(int entityId, double y, float distance,
                                          int fallDamage, TypeConfig config) implements CustomPacketPayload {

    public static final Type<ClientBoundEntityFellPacket> TYPE = new Type<>(SubtleEffects.loc("entity_fell"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundEntityFellPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundEntityFellPacket::entityId,
            ByteBufCodecs.DOUBLE, ClientBoundEntityFellPacket::y,
            ByteBufCodecs.FLOAT, ClientBoundEntityFellPacket::distance,
            ByteBufCodecs.INT, ClientBoundEntityFellPacket::fallDamage,
            TypeConfig.STREAM_CODEC, ClientBoundEntityFellPacket::config,
            ClientBoundEntityFellPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum TypeConfig {
        ENTITY,
        PLAYER,
        MACE,
        ELYTRA;

        public static final StreamCodec<FriendlyByteBuf, TypeConfig> STREAM_CODEC = StreamCodec.of(
                FriendlyByteBuf::writeEnum,
                buf -> buf.readEnum(TypeConfig.class)
        );
    }
}
