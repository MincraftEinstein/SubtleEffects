package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundEntityLandInFluidPayload(int entityId, double y, double yVelocity,
                                                  boolean isLava) implements CustomPacketPayload {

    public static final Type<ClientBoundEntityLandInFluidPayload> TYPE = new Type<>(SubtleEffects.loc("entity_land_in_fluid"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundEntityLandInFluidPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundEntityLandInFluidPayload::entityId,
            ByteBufCodecs.DOUBLE, ClientBoundEntityLandInFluidPayload::y,
            ByteBufCodecs.DOUBLE, ClientBoundEntityLandInFluidPayload::yVelocity,
            ByteBufCodecs.BOOL, ClientBoundEntityLandInFluidPayload::isLava,
            ClientBoundEntityLandInFluidPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
