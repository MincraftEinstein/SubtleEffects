package einstein.subtle_effects.platform.services;

import einstein.subtle_effects.networking.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface NetworkHelper {

    Map<Class<? extends Packet>, PacketData<? extends Packet>> PACKETS = new HashMap<>();

    default <T extends Packet> void registerToClient(ResourceLocation id, Class<T> clazz, Function<FriendlyByteBuf, T> decoder) {
        PACKETS.put(clazz, new PacketData<>(id, Direction.TO_CLIENT, decoder, PacketData.ID++));
    }

    <T extends Packet> void sendToServer(T t);

    <T extends Packet> void sendToClient(T t, ServerPlayer player);

    <T extends Packet> void sendToClientsTracking(ServerLevel level, BlockPos pos, T t);

    <T extends Packet> void sendToClientsTracking(@Nullable ServerPlayer exceptPlayer, ServerLevel level, BlockPos pos, T t);

    enum Direction {
        TO_CLIENT,
        TO_SERVER
    }

    record PacketData<T extends Packet>(ResourceLocation id, Direction direction, Function<FriendlyByteBuf, T> decoder,
                                        int rawId) {

        private static int ID;
    }
}