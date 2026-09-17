package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ClientBoundStonecutterUsedPayload(BlockPos pos, ItemStack stack) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("stonecutter_used");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeItem(stack);
    }

    public static ClientBoundStonecutterUsedPayload read(FriendlyByteBuf buf) {
        return new ClientBoundStonecutterUsedPayload(buf.readBlockPos(), buf.readItem());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
