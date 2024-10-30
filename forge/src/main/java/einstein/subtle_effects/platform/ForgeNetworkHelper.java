package einstein.subtle_effects.platform;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import einstein.subtle_effects.platform.services.NetworkHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ForgeNetworkHelper implements NetworkHelper {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(SubtleEffects.loc("main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    @Override
    public <T extends Packet> void sendToServer(T t) {
        send(t, PacketDistributor.SERVER::noArg);
    }

    @Override
    public <T extends Packet> void sendToClient(T t, ServerPlayer player) {
        send(t, () -> PacketDistributor.PLAYER.with(() -> player));
    }

    @Override
    public <T extends Packet> void sendToClientsTracking(ServerLevel level, BlockPos pos, T t) {
        send(t, () -> PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)));
    }

    @Override
    public <T extends Packet> void sendToClientsTracking(@Nullable ServerPlayer exceptPlayer, ServerLevel level, BlockPos pos, T t) {
        level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(player -> {
            if (!player.equals(exceptPlayer)) {
                sendToClient(t, player);
            }
        });
    }

    private static <T extends Packet> void send(T t, Supplier<PacketDistributor.PacketTarget> target) {
        if (PACKETS.containsKey(t.getClass())) {
            CHANNEL.send(target.get(), t);
            return;
        }
        SubtleEffects.LOGGER.warn("Failed to find packet named: {}", t.id());
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