package einstein.subtle_effects.networking.clientbound;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;

public record ClientBoundSpawnSnoreParticlePacket(double x, double y, double z) implements CustomPacketPayload {

    public static final Type<ClientBoundSpawnSnoreParticlePacket> TYPE = new Type<>(SubtleEffects.loc("spawn_snore_particle"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundSpawnSnoreParticlePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, ClientBoundSpawnSnoreParticlePacket::x,
            ByteBufCodecs.DOUBLE, ClientBoundSpawnSnoreParticlePacket::y,
            ByteBufCodecs.DOUBLE, ClientBoundSpawnSnoreParticlePacket::z,
            ClientBoundSpawnSnoreParticlePacket::new
    );

    public static void handle(PacketContext<ClientBoundSpawnSnoreParticlePacket> context) {
        Level level = Minecraft.getInstance().level;
        if (context.side().equals(Side.CLIENT) && level != null) {
            ClientBoundSpawnSnoreParticlePacket packet = context.message();
            if (ModConfigs.INSTANCE.beehivesHaveSleepingZs.get()) {
                level.addParticle(ModParticles.SNORING.get(), packet.x, packet.y, packet.z, 0, 0, 0);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
