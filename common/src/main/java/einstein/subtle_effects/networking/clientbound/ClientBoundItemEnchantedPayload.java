package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundItemEnchantedPayload(BlockPos pos) implements CustomPacketPayload {

    public static final Type<ClientBoundItemEnchantedPayload> TYPE = new Type<>(SubtleEffects.loc("item_enchanted"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundItemEnchantedPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientBoundItemEnchantedPayload::pos,
            ClientBoundItemEnchantedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
