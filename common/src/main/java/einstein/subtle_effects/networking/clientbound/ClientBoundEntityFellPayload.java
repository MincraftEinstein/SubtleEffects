package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundEntityFellPayload(int entityId, double y, float distance, int fallDamage,
                                           TypeConfig config) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("entity_fell");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeDouble(y);
        buf.writeFloat(distance);
        buf.writeInt(fallDamage);
        buf.writeEnum(config);
    }

    public static ClientBoundEntityFellPayload read(FriendlyByteBuf buf) {
        return new ClientBoundEntityFellPayload(buf.readInt(), buf.readDouble(), buf.readFloat(), buf.readInt(), buf.readEnum(TypeConfig.class));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public enum TypeConfig {
        ENTITY,
        PLAYER,
        MACE,
        ELYTRA;
    }
}
