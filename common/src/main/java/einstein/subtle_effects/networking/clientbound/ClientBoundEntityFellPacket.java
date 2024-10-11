package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public record ClientBoundEntityFellPacket(int entityId, double y, float distance,
                                          int fallDamage) implements Packet {

    public static final ResourceLocation ID = SubtleEffects.loc("entity_fell");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeDouble(y);
        buf.writeFloat(distance);
        buf.writeInt(fallDamage);
    }

    public static ClientBoundEntityFellPacket decode(FriendlyByteBuf buf) {
        return new ClientBoundEntityFellPacket(buf.readInt(), buf.readDouble(), buf.readFloat(), buf.readInt());
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
