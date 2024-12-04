package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundXPBottleEffectsPacket(BlockPos pos) implements CustomPacketPayload {

    public static final Type<ClientBoundXPBottleEffectsPacket> TYPE = new Type<>(SubtleEffects.loc("xp_bottle_effects"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundXPBottleEffectsPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientBoundXPBottleEffectsPacket::pos,
            ClientBoundXPBottleEffectsPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
