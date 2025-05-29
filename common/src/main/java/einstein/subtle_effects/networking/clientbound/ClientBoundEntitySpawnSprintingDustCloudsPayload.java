package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundEntitySpawnSprintingDustCloudsPayload(int entityId) implements CustomPacketPayload {

    public static final Type<ClientBoundEntitySpawnSprintingDustCloudsPayload> TYPE = new Type<>(SubtleEffects.loc("entity_spawn_sprinting_dust_clouds"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundEntitySpawnSprintingDustCloudsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundEntitySpawnSprintingDustCloudsPayload::entityId,
            ClientBoundEntitySpawnSprintingDustCloudsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
