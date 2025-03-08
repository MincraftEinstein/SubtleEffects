package einstein.subtle_effects.platform;

import com.mojang.authlib.GameProfile;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import einstein.subtle_effects.platform.services.NetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FabricNetworkHelper implements NetworkHelper {

    @Override
    public <T extends Packet> void sendToServer(T t) {
        if (ClientPlayNetworking.canSend(t.id())) {
            PacketData<?> data = PACKETS.get(t.getClass());
            FriendlyByteBuf buf = PacketByteBufs.create();
            t.encode(buf);
            ClientPlayNetworking.send(data.id(), buf);
            return;
        }

        SubtleEffects.LOGGER.warn("Unable to send packet to server");
    }

    @Override
    public <T extends Packet> void sendToClient(T t, ServerPlayer player) {
        if (ServerPlayNetworking.canSend(player, t.id())) {
            PacketData<?> data = PACKETS.get(t.getClass());
            FriendlyByteBuf buf = PacketByteBufs.create();
            t.encode(buf);
            ServerPlayNetworking.send(player, data.id(), buf);
            return;
        }

        GameProfile profile = player.getGameProfile();
        SubtleEffects.LOGGER.warn("Unable to send packet to client: {}, {}", profile.getName(), profile.getId());
    }

    @Override
    public <T extends Packet> void sendToClientsTracking(ServerLevel level, BlockPos pos, T t) {
        sendToClientsTracking(null, level, pos, t);
    }

    @Override
    public <T extends Packet> void sendToClientsTracking(@Nullable ServerPlayer exceptPlayer, ServerLevel level, BlockPos pos, T t) {
        PlayerLookup.tracking(level, pos).forEach(player -> {
            if (!player.equals(exceptPlayer)) {
                sendToClient(t, player);
            }
        });
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