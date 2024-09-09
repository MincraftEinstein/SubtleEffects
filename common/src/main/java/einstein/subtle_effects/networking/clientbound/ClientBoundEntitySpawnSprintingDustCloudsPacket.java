package einstein.subtle_effects.networking.clientbound;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.level.Level;

public record ClientBoundEntitySpawnSprintingDustCloudsPacket(int entityId) implements CustomPacketPayload {

    public static final Type<ClientBoundEntitySpawnSprintingDustCloudsPacket> TYPE = new Type<>(SubtleEffects.loc("entity_spawn_sprinting_dust_clouds"));
    public static final StreamCodec<FriendlyByteBuf, ClientBoundEntitySpawnSprintingDustCloudsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientBoundEntitySpawnSprintingDustCloudsPacket::entityId,
            ClientBoundEntitySpawnSprintingDustCloudsPacket::new
    );

    public static void handle(PacketContext<ClientBoundEntitySpawnSprintingDustCloudsPacket> context) {
        if (context.side().equals(Side.CLIENT)) {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                ClientBoundEntitySpawnSprintingDustCloudsPacket packet = context.message();
                if (level.getEntity(packet.entityId) instanceof LivingEntity livingEntity) {
                    int ySpeedModifier = 5;
                    if (livingEntity instanceof Ravager) {
                        ySpeedModifier = 20;
                    }

                    ParticleSpawnUtil.spawnCreatureMovementDustClouds(livingEntity, level, livingEntity.getRandom(), ySpeedModifier);
                }
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
