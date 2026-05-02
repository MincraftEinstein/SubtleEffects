package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundChargedCreeperExplosionPayload(double x, double y, double z, float radius) implements CustomPacketPayload {

    public static final Type<ClientBoundChargedCreeperExplosionPayload> TYPE = new Type<>(SubtleEffects.loc("charged_creeper_explosion"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundChargedCreeperExplosionPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, ClientBoundChargedCreeperExplosionPayload::x,
            ByteBufCodecs.DOUBLE, ClientBoundChargedCreeperExplosionPayload::y,
            ByteBufCodecs.DOUBLE, ClientBoundChargedCreeperExplosionPayload::z,
            ByteBufCodecs.FLOAT, ClientBoundChargedCreeperExplosionPayload::radius,
            ClientBoundChargedCreeperExplosionPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
