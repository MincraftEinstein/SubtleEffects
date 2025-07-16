package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundMooshroomShearedPayload(int entityId) implements CustomPacketPayload {

    public static final Type<ClientBoundMooshroomShearedPayload> TYPE = new Type<>(SubtleEffects.loc("mooshroom_sheared"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundMooshroomShearedPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundMooshroomShearedPayload::entityId,
            ClientBoundMooshroomShearedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
