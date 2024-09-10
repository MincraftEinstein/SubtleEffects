package einstein.subtle_effects.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;

import java.util.function.Consumer;

public interface NetworkHelper {

    <T extends CustomPacketPayload> void registerToClient(CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> streamCodec);

    <T extends CustomPacketPayload> void registerClientHandler(CustomPacketPayload.Type<T> type, Consumer<T> handler);

    <T extends CustomPacketPayload> void sendToClientsTracking(ServerLevel level, BlockPos pos, T packet);
}
