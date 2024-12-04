package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public record ClientBoundXPBottleEffectsPacket(BlockPos pos) implements Packet {

    public static final ResourceLocation ID = SubtleEffects.loc("xp_bottle_effects");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public static ClientBoundXPBottleEffectsPacket decode(FriendlyByteBuf buf) {
        return new ClientBoundXPBottleEffectsPacket(buf.readBlockPos());
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
