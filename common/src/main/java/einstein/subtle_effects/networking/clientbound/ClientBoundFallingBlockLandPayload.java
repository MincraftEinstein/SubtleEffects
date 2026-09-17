package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public record ClientBoundFallingBlockLandPayload(int stateId, BlockPos pos,
                                                 boolean isInWater) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("falling_block_land");

    public ClientBoundFallingBlockLandPayload(BlockState state, BlockPos pos, boolean isInWater) {
        this(Block.getId(state), pos, isInWater);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(stateId);
        buf.writeBlockPos(pos);
        buf.writeBoolean(isInWater);
    }

    public static ClientBoundFallingBlockLandPayload read(FriendlyByteBuf buf) {
        return new ClientBoundFallingBlockLandPayload(buf.readInt(), buf.readBlockPos(), buf.readBoolean());
    }

    public ResourceLocation getId() {
        return ID;
    }
}
