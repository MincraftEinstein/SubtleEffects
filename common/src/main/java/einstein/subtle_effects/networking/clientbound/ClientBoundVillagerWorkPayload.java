package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundVillagerWorkPayload(int villagerId, BlockPos pos) implements CustomPacketPayload {

    public static final Type<ClientBoundVillagerWorkPayload> TYPE = new Type<>(SubtleEffects.loc("villager_work"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundVillagerWorkPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundVillagerWorkPayload::villagerId,
            BlockPos.STREAM_CODEC, ClientBoundVillagerWorkPayload::pos,
            ClientBoundVillagerWorkPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
