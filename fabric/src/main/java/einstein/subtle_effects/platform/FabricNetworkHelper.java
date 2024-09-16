package einstein.subtle_effects.platform;

import einstein.subtle_effects.platform.services.NetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FabricNetworkHelper implements NetworkHelper {

    @Override
    public <T extends CustomPacketPayload> void registerToClient(CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> streamCodec) {
        PayloadTypeRegistry.playS2C().register(type, streamCodec);
    }

    @Override
    public <T extends CustomPacketPayload> void registerClientHandler(CustomPacketPayload.Type<T> type, Consumer<T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> handler.accept(payload));
    }

    @Override
    public <T extends CustomPacketPayload> void sendToClientsTracking(ServerLevel level, BlockPos pos, T packet) {
        sendToClientsTracking(null, level, pos, packet);
    }

    @Override
    public <T extends CustomPacketPayload> void sendToClientsTracking(@Nullable ServerPlayer exceptPlayer, ServerLevel level, BlockPos pos, T packet) {
        PlayerLookup.tracking(level, pos).forEach(player -> {
            if (!player.equals(exceptPlayer) && ServerPlayNetworking.canSend(player, packet.type())) {
                ServerPlayNetworking.send(player, packet);
            }
        });
    }
}
