package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundMooshroomShearedPayload(int entityId) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("mooshroom_sheared");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }

    public static ClientBoundMooshroomShearedPayload read(FriendlyByteBuf buf) {
        return new ClientBoundMooshroomShearedPayload(buf.readInt());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
