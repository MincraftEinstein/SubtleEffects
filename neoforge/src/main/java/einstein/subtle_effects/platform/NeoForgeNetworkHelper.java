package einstein.subtle_effects.platform;

import einstein.subtle_effects.platform.services.NetworkHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NeoForgeNetworkHelper implements NetworkHelper {

    public static final Map<CustomPacketPayload.Type<?>, PayloadData<? extends CustomPacketPayload>> PAYLOAD_DATA = new HashMap<>();

    @Override
    public <T extends CustomPacketPayload> void registerToClient(CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> streamCodec) {
        PAYLOAD_DATA.put(type, new PayloadData<>(streamCodec));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends CustomPacketPayload> void registerClientHandler(CustomPacketPayload.Type<T> type, BiConsumer<ClientLevel, T> handler) {
        ((PayloadData<T>) PAYLOAD_DATA.get(type)).handler = handler;
    }

    @Override
    public <T extends CustomPacketPayload> void sendToClientsTracking(ServerLevel level, BlockPos pos, T packet) {
        PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(pos), packet);
    }

    @Override
    public <T extends CustomPacketPayload> void sendToClientsTracking(@Nullable ServerPlayer exceptPlayer, ServerLevel level, BlockPos pos, T packet) {
        sendToClientsTracking(exceptPlayer, level, pos, packet, null);
    }

    @Override
    public <T extends CustomPacketPayload> void sendToClientsTracking(@Nullable ServerPlayer exceptPlayer, ServerLevel level, BlockPos pos, T packet, @Nullable Consumer<ServerPlayer> skippedPlayerConsumer) {
        ClientboundCustomPayloadPacket payloadPacket = new ClientboundCustomPayloadPacket(packet);
        level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(player -> {
            if (!player.equals(exceptPlayer)) {
                if (player.connection.hasChannel(packet.type().id())) {
                    player.connection.send(payloadPacket);
                    return;
                }

                if (skippedPlayerConsumer != null) {
                    skippedPlayerConsumer.accept(player);
                }
            }
        });
    }

    public static class PayloadData<T extends CustomPacketPayload> {

        public final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
        public BiConsumer<ClientLevel, T> handler;

        public PayloadData(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
            this.streamCodec = streamCodec;
        }
    }
}
