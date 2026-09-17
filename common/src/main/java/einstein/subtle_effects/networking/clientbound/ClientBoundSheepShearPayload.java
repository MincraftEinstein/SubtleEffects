package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public record ClientBoundSheepShearPayload(int entityId) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("sheep_shear");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }

    public static ClientBoundSheepShearPayload read(FriendlyByteBuf buf) {
        return new ClientBoundSheepShearPayload(buf.readInt());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
