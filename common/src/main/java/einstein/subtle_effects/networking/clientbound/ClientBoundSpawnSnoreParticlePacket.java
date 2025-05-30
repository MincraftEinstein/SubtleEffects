package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
public record ClientBoundSpawnSnoreParticlePacket(double x, double y, double z) implements Packet {

    public static final ResourceLocation ID = SubtleEffects.loc("spawn_snore_particle");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public static ClientBoundSpawnSnoreParticlePacket decode(FriendlyByteBuf buf) {
        return new ClientBoundSpawnSnoreParticlePacket(buf.readDouble(), buf.readDouble(), buf.readDouble());
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
