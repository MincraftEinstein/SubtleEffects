package einstein.subtle_effects.init;

import einstein.subtle_effects.networking.clientbound.*;

import static einstein.subtle_effects.platform.Services.NETWORK;

public class ModPayloads {

    public static void init() {
        NETWORK.registerToClient(ClientBoundSpawnSnoreParticlePayload.TYPE, ClientBoundSpawnSnoreParticlePayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundEntityFellPayload.TYPE, ClientBoundEntityFellPayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundEntitySpawnSprintingDustCloudsPayload.TYPE, ClientBoundEntitySpawnSprintingDustCloudsPayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundBlockDestroyEffectsPayload.TYPE, ClientBoundBlockDestroyEffectsPayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundXPBottleEffectsPayload.TYPE, ClientBoundXPBottleEffectsPayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundFallingBlockLandPayload.TYPE, ClientBoundFallingBlockLandPayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundCompostItemPayload.TYPE, ClientBoundCompostItemPayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundStonecutterUsedPayload.TYPE, ClientBoundStonecutterUsedPayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundVillagerWorkPayload.TYPE, ClientBoundVillagerWorkPayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundMooshroomShearedPayload.TYPE, ClientBoundMooshroomShearedPayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundAnimalFedPayload.TYPE, ClientBoundAnimalFedPayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundDrankPotionPayload.TYPE, ClientBoundDrankPotionPayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundDispenseBucketPayload.TYPE, ClientBoundDispenseBucketPayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundSheepShearPayload.TYPE, ClientBoundSheepShearPayload.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundEntityLandInFluidPayload.TYPE, ClientBoundEntityLandInFluidPayload.STREAM_CODEC);
    }

    public static void initClientHandlers() {
        NETWORK.registerClientHandler(ClientBoundSpawnSnoreParticlePayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundEntityFellPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundEntitySpawnSprintingDustCloudsPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundBlockDestroyEffectsPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundXPBottleEffectsPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundFallingBlockLandPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundCompostItemPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundStonecutterUsedPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundVillagerWorkPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundMooshroomShearedPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundAnimalFedPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundDrankPotionPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundDispenseBucketPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundSheepShearPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundEntityLandInFluidPayload.TYPE, ClientPacketHandlers::handle);
    }
}
