package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

public record ClientBoundAnimalFedPayload(int animalId, ItemStack stack) implements CustomPacketPayload {

    public static final Type<ClientBoundAnimalFedPayload> TYPE = new Type<>(SubtleEffects.loc("animal_fed"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundAnimalFedPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundAnimalFedPayload::animalId,
            ItemStack.STREAM_CODEC, ClientBoundAnimalFedPayload::stack,
            ClientBoundAnimalFedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
