package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ClientBoundAnimalFedPacket(int animalId, ItemStack stack) implements Packet {

    public static final ResourceLocation ID = SubtleEffects.loc("animal_fed");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(animalId);
        buf.writeItem(stack);
    }

    public static ClientBoundAnimalFedPacket decode(FriendlyByteBuf buf) {
        return new ClientBoundAnimalFedPacket(buf.readInt(), buf.readItem());
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
