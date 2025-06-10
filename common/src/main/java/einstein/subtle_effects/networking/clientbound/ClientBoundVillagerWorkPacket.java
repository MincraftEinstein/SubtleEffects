package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public record ClientBoundVillagerWorkPacket(int villagerId, BlockPos pos) implements Packet {

    public static final ResourceLocation ID = SubtleEffects.loc("villager_work");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(villagerId);
        buf.writeBlockPos(pos);
    }

    public static ClientBoundVillagerWorkPacket decode(FriendlyByteBuf buf) {
        return new ClientBoundVillagerWorkPacket(buf.readInt(), buf.readBlockPos());
    }

    @Override
    public void handle(@Nullable ServerPlayer player) {
        ClientPacketHandlers.handle(this);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
