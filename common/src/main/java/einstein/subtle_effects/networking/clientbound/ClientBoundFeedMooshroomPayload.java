package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.component.SuspiciousStewEffects;

import java.util.List;

public record ClientBoundFeedMooshroomPayload(int entityId,
                                              List<SuspiciousStewEffects.Entry> effects) implements CustomPacketPayload {

    public static final Type<ClientBoundFeedMooshroomPayload> TYPE = new Type<>(SubtleEffects.loc("feed_mooshroom"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundFeedMooshroomPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundFeedMooshroomPayload::entityId,
            ByteBufCodecs.<RegistryFriendlyByteBuf, SuspiciousStewEffects.Entry>list().apply(SuspiciousStewEffects.Entry.STREAM_CODEC), ClientBoundFeedMooshroomPayload::effects,
            ClientBoundFeedMooshroomPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
