package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundCopperGolemPayload(int entityId, Action action) implements CustomPacketPayload {

    public static final Type<ClientBoundCopperGolemPayload> TYPE = new Type<>(SubtleEffects.loc("copper_golem"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundCopperGolemPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundCopperGolemPayload::entityId,
            Action.STREAM_CODEC, ClientBoundCopperGolemPayload::action,
            ClientBoundCopperGolemPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Action {
        WAX_ON,
        WAX_OFF,
        SCRAPE;

        public static final StreamCodec<FriendlyByteBuf, Action> STREAM_CODEC = StreamCodec.of(
                FriendlyByteBuf::writeEnum,
                buf -> buf.readEnum(Action.class)
        );
    }
}
