package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.networking.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ClientBoundDispenseBucketPayload(ItemStack stack, BlockPos pos) implements Packet {

    public static final ResourceLocation ID = SubtleEffects.loc("dispense_bucket");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(stack);
        buf.writeBlockPos(pos);
    }

    public static ClientBoundDispenseBucketPayload decode(FriendlyByteBuf buf) {
        return new ClientBoundDispenseBucketPayload(buf.readItem(), buf.readBlockPos());
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
