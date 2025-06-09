package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ClientBoundCompostItemPayload(ItemStack stack, BlockPos pos, boolean wasFarmer) implements Packet {

    public static final ResourceLocation ID = SubtleEffects.loc("compost_item");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(stack);
        buf.writeBlockPos(pos);
        buf.writeBoolean(wasFarmer);
    }

    public static ClientBoundCompostItemPayload decode(FriendlyByteBuf buf) {
        return new ClientBoundCompostItemPayload(buf.readItem(), buf.readBlockPos(), buf.readBoolean());
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
