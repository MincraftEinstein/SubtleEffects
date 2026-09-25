package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundEntityLandInFluidPayload(int entityId, double y, double yVelocity,
                                                  BlockPos pos, boolean isCauldron) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("entity_land_in_fluid");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeDouble(y);
        buf.writeDouble(yVelocity);
        buf.writeBlockPos(pos);
        buf.writeBoolean(isCauldron);
    }

    public static ClientBoundEntityLandInFluidPayload read(FriendlyByteBuf buf) {
        return new ClientBoundEntityLandInFluidPayload(buf.readInt(), buf.readDouble(), buf.readDouble(), buf.readBlockPos(), buf.readBoolean());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
