package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public record ClientBoundEntitySpawnSprintingDustCloudsPacket(
        int entityId) implements Packet {

    public static final ResourceLocation ID = SubtleEffects.loc("entity_spawn_sprinting_dust_clouds");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }

    public static ClientBoundEntitySpawnSprintingDustCloudsPacket decode(FriendlyByteBuf buf) {
        return new ClientBoundEntitySpawnSprintingDustCloudsPacket(buf.readInt());
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
