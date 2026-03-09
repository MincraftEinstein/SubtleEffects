package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public record ClientBoundMobSpawnerSpawnPayload(BlockPos pos) implements Packet {

    public static final ResourceLocation ID = SubtleEffects.loc("mob_spawner_spawn");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public static ClientBoundMobSpawnerSpawnPayload decode(FriendlyByteBuf buf) {
        return new ClientBoundMobSpawnerSpawnPayload(buf.readBlockPos());
    }

    @Override
    public void handle(@Nullable ServerPlayer player) {
        ClientPacketHandlers.handle(this);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
