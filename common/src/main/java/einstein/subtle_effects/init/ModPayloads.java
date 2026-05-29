package einstein.subtle_effects.init;

import einstein.subtle_effects.networking.ClientPayloadHandlers;
import einstein.subtle_effects.networking.clientbound.*;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;

import java.util.function.BiConsumer;

public class ModPayloads {

    public static void init() {
        registerToClient(ClientBoundSpawnSnoreParticlePayload.TYPE, ClientBoundSpawnSnoreParticlePayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundEntityFellPayload.TYPE, ClientBoundEntityFellPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundEntitySpawnSprintingDustCloudsPayload.TYPE, ClientBoundEntitySpawnSprintingDustCloudsPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundBlockDestroyEffectsPayload.TYPE, ClientBoundBlockDestroyEffectsPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundXPBottleEffectsPayload.TYPE, ClientBoundXPBottleEffectsPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundFallingBlockLandPayload.TYPE, ClientBoundFallingBlockLandPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundCompostItemPayload.TYPE, ClientBoundCompostItemPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundStonecutterUsedPayload.TYPE, ClientBoundStonecutterUsedPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundVillagerWorkPayload.TYPE, ClientBoundVillagerWorkPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundMooshroomShearedPayload.TYPE, ClientBoundMooshroomShearedPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundAnimalFedPayload.TYPE, ClientBoundAnimalFedPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundDrankPotionPayload.TYPE, ClientBoundDrankPotionPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundDispenseBucketPayload.TYPE, ClientBoundDispenseBucketPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundSheepShearPayload.TYPE, ClientBoundSheepShearPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundEntityLandInFluidPayload.TYPE, ClientBoundEntityLandInFluidPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundMobSpawnerSpawnPayload.TYPE, ClientBoundMobSpawnerSpawnPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundEntityDamagedPayload.TYPE, ClientBoundEntityDamagedPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundChargedCreeperExplosionPayload.TYPE, ClientBoundChargedCreeperExplosionPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundItemEnchantedPayload.TYPE, ClientBoundItemEnchantedPayload.STREAM_CODEC, ClientPayloadHandlers::handle);
    }

    private static <T extends CustomPacketPayload> void registerToClient(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec, BiConsumer<Level, T> handler) {
        ConfigApiJava.network().registerLenientS2C(type, streamCodec, (payload, context) -> context.execute(() -> handler.accept(context.player().level(), payload)));
    }
}
