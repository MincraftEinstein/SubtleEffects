package einstein.subtle_effects.platform;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import einstein.subtle_effects.platform.services.NetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FabricNetworkHelper implements NetworkHelper {

    @Override
    public <T extends Packet> boolean sendToServer(T t) {
        return send(t, ClientPlayNetworking.canSend(t.id()), ClientPlayNetworking::send);
    }

    @Override
    public <T extends Packet> boolean sendToClient(T t, ServerPlayer player) {
        return send(t, ServerPlayNetworking.canSend(player, t.id()), (id, buf) -> ServerPlayNetworking.send(player, id, buf));
    }

    @Override
    public <T extends Packet> void sendToClientsTracking(ServerLevel level, BlockPos pos, T t) {
        sendToClientsTracking(null, level, pos, t);
    }

    @Override
    public <T extends Packet> void sendToClientsTracking(@Nullable ServerPlayer exceptPlayer, ServerLevel level, BlockPos pos, T t) {
        sendToClientsTracking(exceptPlayer, level, pos, t, player -> {});
    }

    @Override
    public <T extends Packet> void sendToClientsTracking(@Nullable ServerPlayer exceptPlayer, ServerLevel level, BlockPos pos, T t, @Nullable Consumer<ServerPlayer> skippedPlayerConsumer) {
        PlayerLookup.tracking(level, pos).forEach(player -> {
            if (!player.equals(exceptPlayer)) {
                if (!sendToClient(t, player) && skippedPlayerConsumer != null) {
                    skippedPlayerConsumer.accept(player);
                }
            }
        });
    }

    private static <T extends Packet> boolean send(T t, boolean canSend, BiConsumer<ResourceLocation, FriendlyByteBuf> consumer) {
        Class<? extends Packet> packetClass = t.getClass();
        ResourceLocation id = t.id();
        if (canSend) {
            if (PACKETS.containsKey(packetClass)) {
                PacketData<?> data = PACKETS.get(packetClass);
                FriendlyByteBuf buf = PacketByteBufs.create();
                t.encode(buf);
                consumer.accept(data.id(), buf);
                return true;
            }
            SubtleEffects.LOGGER.warn("Failed to find packet named: {}", id);
        }
        return false;
    }

    public static void init(Direction directionToRegister) {
        PACKETS.forEach((clazz, data) -> {
            if (data.direction() == directionToRegister) {
                if (directionToRegister == Direction.TO_CLIENT) {
                    ClientPlayNetworking.registerGlobalReceiver(data.id(), (minecraft, handler, buf, responseSender) -> {
                        Packet packet = data.decoder().apply(buf);
                        minecraft.execute(() -> packet.handle(null));
                    });
                }
                else if (directionToRegister == Direction.TO_SERVER) {
                    ServerPlayNetworking.registerGlobalReceiver(data.id(), (server, player, handler, buf, responseSender) -> {
                        Packet packet = data.decoder().apply(buf);
                        server.execute(() -> packet.handle(player));
                    });
                }
            }
        });
    }
}