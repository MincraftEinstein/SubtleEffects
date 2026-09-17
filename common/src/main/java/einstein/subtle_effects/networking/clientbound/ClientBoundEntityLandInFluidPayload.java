package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public record ClientBoundEntityLandInFluidPayload(int entityId, double y, double yVelocity,
                                                  Fluid fluid) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("entity_land_in_fluid");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeDouble(y);
        buf.writeDouble(yVelocity);
        buf.writeResourceLocation(BuiltInRegistries.FLUID.getKey(fluid));
    }

    public static ClientBoundEntityLandInFluidPayload read(FriendlyByteBuf buf) {
        return new ClientBoundEntityLandInFluidPayload(buf.readInt(), buf.readDouble(), buf.readDouble(), BuiltInRegistries.FLUID.get(buf.readResourceLocation()));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
