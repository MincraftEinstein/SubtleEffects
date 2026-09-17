package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundChargedCreeperExplosionPayload(double x, double y, double z,
                                                        float radius) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("charged_creeper_explosion");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeFloat(radius);
    }

    public static ClientBoundChargedCreeperExplosionPayload read(FriendlyByteBuf buf) {
        return new ClientBoundChargedCreeperExplosionPayload(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
