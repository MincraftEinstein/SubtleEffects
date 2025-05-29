package einstein.subtle_effects.platform;

import einstein.subtle_effects.platform.services.NetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FabricNetworkHelper implements NetworkHelper {

    @Override
    public <T extends CustomPacketPayload> void registerToClient(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        PayloadTypeRegistry.playS2C().register(type, streamCodec);
    }

    @Override
    public <T extends CustomPacketPayload> void registerClientHandler(CustomPacketPayload.Type<T> type, BiConsumer<ClientLevel, T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                handler.accept(level, payload);
            }
        });
    }

    @Override
    public <T extends CustomPacketPayload> void sendToClientsTracking(ServerLevel level, BlockPos pos, T packet) {
        sendToClientsTracking(null, level, pos, packet);
    }

    @Override
    public <T extends CustomPacketPayload> void sendToClientsTracking(@Nullable ServerPlayer exceptPlayer, ServerLevel level, BlockPos pos, T packet) {
        sendToClientsTracking(exceptPlayer, level, pos, packet, null);
    }

    @Override
    public <T extends CustomPacketPayload> void sendToClientsTracking(@Nullable ServerPlayer exceptPlayer, ServerLevel level, BlockPos pos, T packet, @Nullable Consumer<ServerPlayer> skippedPlayerConsumer) {
        PlayerLookup.tracking(level, pos).forEach(player -> {
            if (!player.equals(exceptPlayer)) {
                if (ServerPlayNetworking.canSend(player, packet.type())) {
                    ServerPlayNetworking.send(player, packet);
                    return;
                }

                if (skippedPlayerConsumer != null) {
                    skippedPlayerConsumer.accept(player);
                }
            }
        });
    }
}
