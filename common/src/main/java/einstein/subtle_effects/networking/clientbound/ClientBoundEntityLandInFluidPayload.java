package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public record ClientBoundEntityLandInFluidPayload(int entityId, double y, double yVelocity,
                                                  boolean isLava) implements Packet {

    public static final ResourceLocation ID = SubtleEffects.loc("entity_land_in_fluid");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeDouble(y);
        buf.writeDouble(yVelocity);
        buf.writeBoolean(isLava);
    }

    public static ClientBoundEntityLandInFluidPayload decode(FriendlyByteBuf buf) {
        return new ClientBoundEntityLandInFluidPayload(buf.readInt(), buf.readDouble(), buf.readDouble(), buf.readBoolean());
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
