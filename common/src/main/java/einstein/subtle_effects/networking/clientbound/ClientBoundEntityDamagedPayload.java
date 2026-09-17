package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record ClientBoundEntityDamagedPayload(int entityId,
                                              Optional<ResourceLocation> damageType) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("entity_damaged");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeOptional(damageType, (buf1, resourceLocation) -> buf.writeResourceLocation(resourceLocation));
    }

    public static ClientBoundEntityDamagedPayload read(FriendlyByteBuf buf) {
        return new ClientBoundEntityDamagedPayload(buf.readInt(), buf.readOptional(buf1 -> buf.readResourceLocation()));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
