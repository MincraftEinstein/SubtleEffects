package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record ClientBoundDispenseBucketPayload(ItemStack stack, BlockPos pos,
                                               boolean hasVanillaFallback) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("dispense_bucket");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeItem(stack);
        buf.writeBlockPos(pos);
        buf.writeBoolean(hasVanillaFallback);
    }

    public static ClientBoundDispenseBucketPayload read(FriendlyByteBuf buf) {
        return new ClientBoundDispenseBucketPayload(buf.readItem(), buf.readBlockPos(), buf.readBoolean());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
