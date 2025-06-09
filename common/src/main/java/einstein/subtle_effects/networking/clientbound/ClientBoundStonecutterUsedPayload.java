package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record ClientBoundStonecutterUsedPayload(BlockPos pos, ItemStack stack) implements CustomPacketPayload {

    public static final Type<ClientBoundStonecutterUsedPayload> TYPE = new Type<>(SubtleEffects.loc("stonecutter_used"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundStonecutterUsedPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientBoundStonecutterUsedPayload::pos,
            ItemStack.STREAM_CODEC, ClientBoundStonecutterUsedPayload::stack,
            ClientBoundStonecutterUsedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
