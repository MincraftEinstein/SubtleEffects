package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundDecoratedPotInteractionPayload(BlockPos pos) implements CustomPacketPayload {

    public static final Type<ClientBoundDecoratedPotInteractionPayload> TYPE = new Type<>(SubtleEffects.loc("decorated_pot_interaction"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundDecoratedPotInteractionPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientBoundDecoratedPotInteractionPayload::pos,
            ClientBoundDecoratedPotInteractionPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
