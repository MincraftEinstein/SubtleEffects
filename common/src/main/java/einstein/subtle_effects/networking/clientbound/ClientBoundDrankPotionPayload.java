package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundDrankPotionPayload(int entityId) implements CustomPacketPayload {

    public static final Type<ClientBoundDrankPotionPayload> TYPE = new Type<>(SubtleEffects.loc("drank_potion"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundDrankPotionPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundDrankPotionPayload::entityId,
            ClientBoundDrankPotionPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
