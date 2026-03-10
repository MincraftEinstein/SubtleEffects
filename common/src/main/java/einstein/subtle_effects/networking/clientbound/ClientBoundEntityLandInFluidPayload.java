package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.material.Fluid;

public record ClientBoundEntityLandInFluidPayload(int entityId, double y, double yVelocity,
                                                  Fluid fluid) implements CustomPacketPayload {

    public static final Type<ClientBoundEntityLandInFluidPayload> TYPE = new Type<>(SubtleEffects.loc("entity_land_in_fluid"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundEntityLandInFluidPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundEntityLandInFluidPayload::entityId,
            ByteBufCodecs.DOUBLE, ClientBoundEntityLandInFluidPayload::y,
            ByteBufCodecs.DOUBLE, ClientBoundEntityLandInFluidPayload::yVelocity,
            ByteBufCodecs.registry(Registries.FLUID), ClientBoundEntityLandInFluidPayload::fluid,
            ClientBoundEntityLandInFluidPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
