package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundEntitySpawnSprintingDustCloudsPayload(int entityId) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("entity_spawn_sprinting_dust_clouds");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }

    public static ClientBoundEntitySpawnSprintingDustCloudsPayload read(FriendlyByteBuf buf) {
        return new ClientBoundEntitySpawnSprintingDustCloudsPayload(buf.readInt());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
