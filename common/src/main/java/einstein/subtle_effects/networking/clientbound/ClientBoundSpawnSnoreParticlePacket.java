package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundSpawnSnoreParticlePacket(double x, double y, double z) implements CustomPacketPayload {

    public static final Type<ClientBoundSpawnSnoreParticlePacket> TYPE = new Type<>(SubtleEffects.loc("spawn_snore_particle"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundSpawnSnoreParticlePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, ClientBoundSpawnSnoreParticlePacket::x,
            ByteBufCodecs.DOUBLE, ClientBoundSpawnSnoreParticlePacket::y,
            ByteBufCodecs.DOUBLE, ClientBoundSpawnSnoreParticlePacket::z,
            ClientBoundSpawnSnoreParticlePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
