package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record ClientBoundEntityDamagedPayload(int entityId,
                                              Optional<ResourceLocation> damageType) implements CustomPacketPayload {

    public static final Type<ClientBoundEntityDamagedPayload> TYPE = new Type<>(SubtleEffects.loc("entity_damaged"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundEntityDamagedPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundEntityDamagedPayload::entityId,
            ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), ClientBoundEntityDamagedPayload::damageType,
            ClientBoundEntityDamagedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
