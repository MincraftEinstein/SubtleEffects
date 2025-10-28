package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;

public record ClientBoundExplosionPayload(Vec3 position, float radius) implements CustomPacketPayload {

    public static final Type<ClientBoundExplosionPayload> TYPE = new Type<>(SubtleEffects.loc("explosion"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundExplosionPayload> STREAM_CODEC = StreamCodec.composite(
            Vec3.STREAM_CODEC, ClientBoundExplosionPayload::position,
            ByteBufCodecs.FLOAT, ClientBoundExplosionPayload::radius,
            ClientBoundExplosionPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
