package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundXPBottleEffectsPayload(BlockPos pos) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("xp_bottle_effects");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public static ClientBoundXPBottleEffectsPayload read(FriendlyByteBuf buf) {
        return new ClientBoundXPBottleEffectsPayload(buf.readBlockPos());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
