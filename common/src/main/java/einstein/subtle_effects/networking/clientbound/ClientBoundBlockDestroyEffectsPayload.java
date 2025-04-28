package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record ClientBoundBlockDestroyEffectsPayload(int stateId, BlockPos pos, TypeConfig config) implements CustomPacketPayload {

    public static final Type<ClientBoundBlockDestroyEffectsPayload> TYPE = new Type<>(SubtleEffects.loc("block_destroy_effects"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundBlockDestroyEffectsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundBlockDestroyEffectsPayload::stateId,
            BlockPos.STREAM_CODEC, ClientBoundBlockDestroyEffectsPayload::pos,
            TypeConfig.STREAM_CODEC, ClientBoundBlockDestroyEffectsPayload::config,
            ClientBoundBlockDestroyEffectsPayload::new
    );

    public ClientBoundBlockDestroyEffectsPayload(BlockState state, BlockPos pos, TypeConfig config) {
        this(Block.getId(state), pos, config);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum TypeConfig {
        LEAVES_DECAY,
        FARMLAND_DESTROY;

        public static final StreamCodec<FriendlyByteBuf, TypeConfig> STREAM_CODEC = StreamCodec.of(
                FriendlyByteBuf::writeEnum,
                buf -> buf.readEnum(TypeConfig.class)
        );
    }
}
