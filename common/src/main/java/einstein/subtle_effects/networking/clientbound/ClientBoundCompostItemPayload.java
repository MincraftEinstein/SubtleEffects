package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ClientBoundCompostItemPayload(ItemStack stack, BlockPos pos, boolean wasFarmer) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("compost_item");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeItem(stack);
        buf.writeBlockPos(pos);
        buf.writeBoolean(wasFarmer);
    }

    public static ClientBoundCompostItemPayload read(FriendlyByteBuf buf) {
        return new ClientBoundCompostItemPayload(buf.readItem(), buf.readBlockPos(), buf.readBoolean());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
