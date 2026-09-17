package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundVillagerWorkPayload(int villagerId, BlockPos pos) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("villager_work");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(villagerId);
        buf.writeBlockPos(pos);
    }

    public static ClientBoundVillagerWorkPayload read(FriendlyByteBuf buf) {
        return new ClientBoundVillagerWorkPayload(buf.readInt(), buf.readBlockPos());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
