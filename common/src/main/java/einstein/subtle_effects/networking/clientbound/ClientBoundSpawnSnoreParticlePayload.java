package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundSpawnSnoreParticlePayload(double x, double y, double z) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("spawn_snore_particle");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public static ClientBoundSpawnSnoreParticlePayload read(FriendlyByteBuf buf) {
        return new ClientBoundSpawnSnoreParticlePayload(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
