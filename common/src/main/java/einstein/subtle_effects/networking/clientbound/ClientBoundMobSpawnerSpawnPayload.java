package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundMobSpawnerSpawnPayload(BlockPos pos) implements CustomPacketPayload {

    public static final Type<ClientBoundMobSpawnerSpawnPayload> TYPE = new Type<>(SubtleEffects.loc("mob_spawner_spawn"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundMobSpawnerSpawnPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientBoundMobSpawnerSpawnPayload::pos,
            ClientBoundMobSpawnerSpawnPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
