package einstein.subtle_effects.networking;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PayloadSender {

    public static <T extends CustomPacketPayload> void sendToClientsTracking(ServerLevel level, BlockPos pos, T payload) {
        sendToClientsTracking(null, level, pos, payload);
    }

    public static <T extends CustomPacketPayload> void sendToClientsTracking(@Nullable ServerPlayer skippedPlayer, ServerLevel level, BlockPos pos, T payload) {
        sendToClientsTracking(skippedPlayer, level, pos, payload, null);
    }

    public static <T extends CustomPacketPayload> void sendToClientsTracking(@Nullable ServerPlayer skippedPlayer, ServerLevel level, BlockPos pos, T payload, @Nullable Consumer<ServerPlayer> failedPlayersHandler) {
        level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(player -> {
            if (!player.equals(skippedPlayer)) {
                if (ConfigApiJava.network().canSend(payload.type().id(), player)) {
                    ConfigApiJava.network().send(payload, player);
                    return;
                }

                if (failedPlayersHandler != null) {
                    failedPlayersHandler.accept(player);
                }
            }
        });
    }
}
