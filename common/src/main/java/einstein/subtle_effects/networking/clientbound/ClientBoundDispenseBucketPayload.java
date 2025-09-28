package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

public record ClientBoundDispenseBucketPayload(ItemStack stack, BlockPos pos) implements CustomPacketPayload {

    public static final Type<ClientBoundDispenseBucketPayload> TYPE = new Type<>(SubtleEffects.loc("dispense_bucket"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundDispenseBucketPayload> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, ClientBoundDispenseBucketPayload::stack,
            BlockPos.STREAM_CODEC, ClientBoundDispenseBucketPayload::pos,
            ClientBoundDispenseBucketPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
