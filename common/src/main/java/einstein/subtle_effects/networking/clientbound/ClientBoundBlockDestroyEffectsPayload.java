package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record ClientBoundBlockDestroyEffectsPayload(int stateId, BlockPos pos, TypeConfig config) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("block_destroy_effects");

    public ClientBoundBlockDestroyEffectsPayload(BlockState state, BlockPos pos, TypeConfig config) {
        this(Block.getId(state), pos, config);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(stateId);
        buf.writeBlockPos(pos);
        buf.writeEnum(config);
    }

    public static ClientBoundBlockDestroyEffectsPayload read(FriendlyByteBuf buf) {
        return new ClientBoundBlockDestroyEffectsPayload(buf.readInt(), buf.readBlockPos(), buf.readEnum(TypeConfig.class));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public enum TypeConfig {
        LEAVES_DECAY,
        FARMLAND_DESTROY;
    }
}
