package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public record ClientBoundFallingBlockLandPayload(int stateId, BlockPos pos) implements Packet {

    public static final ResourceLocation ID = SubtleEffects.loc("falling_block_land");

    public ClientBoundFallingBlockLandPayload(BlockState state, BlockPos pos) {
        this(Block.getId(state), pos);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(stateId);
        buf.writeBlockPos(pos);
    }

    public static ClientBoundFallingBlockLandPayload decode(FriendlyByteBuf buf) {
        return new ClientBoundFallingBlockLandPayload(buf.readInt(), buf.readBlockPos());
    }

    @Override
    public void handle(@Nullable ServerPlayer player) {
        ClientPacketHandlers.handle(this);
    }

    public ResourceLocation id() {
        return ID;
    }
}
