package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record ClientBoundAnimalFedPayload(int animalId, ItemStack stack) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("animal_fed");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(animalId);
        buf.writeItem(stack);
    }

    public static ClientBoundAnimalFedPayload read(FriendlyByteBuf buf) {
        return new ClientBoundAnimalFedPayload(buf.readInt(), buf.readItem());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
