package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record ClientBoundFallingBlockLandPayload(int stateId, BlockPos pos) implements CustomPacketPayload {

    public static final Type<ClientBoundFallingBlockLandPayload> TYPE = new Type<>(SubtleEffects.loc("falling_block_land"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundFallingBlockLandPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundFallingBlockLandPayload::stateId,
            BlockPos.STREAM_CODEC, ClientBoundFallingBlockLandPayload::pos,
            ClientBoundFallingBlockLandPayload::new
    );

    public ClientBoundFallingBlockLandPayload(BlockState state, BlockPos pos) {
        this(Block.getId(state), pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
