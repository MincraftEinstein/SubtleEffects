package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundXPBottleEffectsPayload(BlockPos pos) implements CustomPacketPayload {

    public static final Type<ClientBoundXPBottleEffectsPayload> TYPE = new Type<>(SubtleEffects.loc("xp_bottle_effects"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundXPBottleEffectsPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientBoundXPBottleEffectsPayload::pos,
            ClientBoundXPBottleEffectsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
