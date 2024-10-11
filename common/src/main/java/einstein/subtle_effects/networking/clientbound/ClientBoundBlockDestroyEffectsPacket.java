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

public record ClientBoundBlockDestroyEffectsPacket(int stateId, BlockPos pos, TypeConfig config) implements Packet {

    public static final ResourceLocation ID = SubtleEffects.loc("block_destroy_effects");

    public ClientBoundBlockDestroyEffectsPacket(BlockState state, BlockPos pos, TypeConfig config) {
        this(Block.getId(state), pos, config);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(stateId);
        buf.writeBlockPos(pos);
        buf.writeEnum(config);
    }

    public static ClientBoundBlockDestroyEffectsPacket decode(FriendlyByteBuf buf) {
        return new ClientBoundBlockDestroyEffectsPacket(buf.readInt(), buf.readBlockPos(), buf.readEnum(TypeConfig.class));
    }

    @Override
    public void handle(@Nullable ServerPlayer player) {
        ClientPacketHandlers.handle(this);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public enum TypeConfig {
        LEAVES_DECAY,
        FARMLAND_DESTROY;
    }
}
