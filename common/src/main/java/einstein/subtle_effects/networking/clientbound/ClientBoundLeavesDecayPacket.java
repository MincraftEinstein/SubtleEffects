package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundLeavesDecayPacket(int stateId, BlockPos pos) implements CustomPacketPayload {

    public static final Type<ClientBoundLeavesDecayPacket> TYPE = new Type<>(SubtleEffects.loc("leaves_decay"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundLeavesDecayPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundLeavesDecayPacket::stateId,
            BlockPos.STREAM_CODEC, ClientBoundLeavesDecayPacket::pos,
            ClientBoundLeavesDecayPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
