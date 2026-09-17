package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundItemEnchantedPayload(BlockPos pos) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("item_enchanted");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public static ClientBoundItemEnchantedPayload read(FriendlyByteBuf buf) {
        return new ClientBoundItemEnchantedPayload(buf.readBlockPos());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
