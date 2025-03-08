package einstein.subtle_effects.platform;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import einstein.subtle_effects.platform.services.NetworkHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ForgeNetworkHelper implements NetworkHelper {

    private static final String PROTOCOL_VERSION = "1";
    public static final Predicate<String> VERSION_MATCHER = NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION);
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(SubtleEffects.loc("main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(VERSION_MATCHER)
            .serverAcceptedVersions(VERSION_MATCHER)
            .simpleChannel();

    @Override
    public <T extends Packet> boolean sendToServer(T t) {
        ClientPacketListener listener = Minecraft.getInstance().getConnection();
        if (listener != null) {
            return send(t, listener.getConnection(), PacketDistributor.SERVER::noArg);
        }
        return false;
    }

    @Override
    public <T extends Packet> boolean sendToClient(T t, ServerPlayer player) {
        return send(t, player.connection.connection, () -> PacketDistributor.PLAYER.with(() -> player));
    }

    @Override
    public <T extends Packet> void sendToClientsTracking(ServerLevel level, BlockPos pos, T t) {
        sendToClientsTracking(null, level, pos, t);
    }

    @Override
    public <T extends Packet> void sendToClientsTracking(@Nullable ServerPlayer exceptPlayer, ServerLevel level, BlockPos pos, T t) {
        sendToClientsTracking(exceptPlayer, level, pos, t, null);
    }

    @Override
    public <T extends Packet> void sendToClientsTracking(@Nullable ServerPlayer exceptPlayer, ServerLevel level, BlockPos pos, T t, @Nullable Consumer<ServerPlayer> skippedPlayersConsumer) {
        level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(player -> {
            if (!player.equals(exceptPlayer)) {
                if (!sendToClient(t, player) && skippedPlayersConsumer != null) {
                    skippedPlayersConsumer.accept(player);
                }
            }
        });
    }

    private static <T extends Packet> boolean send(T t, Connection connection, Supplier<PacketDistributor.PacketTarget> target) {
        if (CHANNEL.isRemotePresent(connection)) {
            if (PACKETS.containsKey(t.getClass())) {
                CHANNEL.send(target.get(), t);
                return true;
            }
            SubtleEffects.LOGGER.warn("Failed to find packet named: {}", t.id());
        }
        return false;
    }

    public static void init() {
        PACKETS.forEach(ForgeNetworkHelper::registerPacket);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Packet> void registerPacket(Class<?> clazz, PacketData<? extends Packet> packetData) {
        PacketData<T> data = (PacketData<T>) packetData;
        CHANNEL.messageBuilder((Class<T>) clazz, data.rawId(), getDirectionToNetworkDirection(data))
                .encoder(Packet::encode)
                .decoder(buf -> data.decoder().apply(buf))
                .consumerMainThread((packet, context) -> packet.handle(context.get().getSender()))
                .add();
    }

    private static NetworkDirection getDirectionToNetworkDirection(PacketData<?> data) {
        return data.direction() == Direction.TO_CLIENT ? NetworkDirection.PLAY_TO_CLIENT : NetworkDirection.PLAY_TO_SERVER;
    }
}