package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

public record ClientBoundCompostItemPayload(ItemStack stack, BlockPos pos) implements CustomPacketPayload {

    public static final Type<ClientBoundCompostItemPayload> TYPE = new Type<>(SubtleEffects.loc("compost_item"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundCompostItemPayload> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, ClientBoundCompostItemPayload::stack,
            BlockPos.STREAM_CODEC, ClientBoundCompostItemPayload::pos,
            ClientBoundCompostItemPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
