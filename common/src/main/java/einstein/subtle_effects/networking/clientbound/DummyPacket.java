package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class DummyPacket implements CustomPacketPayload {

    public static final Type<DummyPacket> TYPE = new Type<>(SubtleEffects.loc("dummy_packet"));
    public static final StreamCodec<FriendlyByteBuf, DummyPacket> STREAM_CODEC = StreamCodec.of((o, packet) -> {}, buf -> new DummyPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(DummyPacket packet) {

    }
}
