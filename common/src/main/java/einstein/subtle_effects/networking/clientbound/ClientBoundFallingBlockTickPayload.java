package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundFallingBlockTickPayload(int entityId, double fallDistance) implements CustomPacketPayload {

    public static final Type<ClientBoundFallingBlockTickPayload> TYPE = new Type<>(SubtleEffects.loc("falling_block_tick"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundFallingBlockTickPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundFallingBlockTickPayload::entityId,
            ByteBufCodecs.DOUBLE, ClientBoundFallingBlockTickPayload::fallDistance,
            ClientBoundFallingBlockTickPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
