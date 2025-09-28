package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundSheepShearPayload(int entityId) implements CustomPacketPayload {

    public static final Type<ClientBoundSheepShearPayload> TYPE = new Type<>(SubtleEffects.loc("sheep_shear"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundSheepShearPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundSheepShearPayload::entityId,
            ClientBoundSheepShearPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
