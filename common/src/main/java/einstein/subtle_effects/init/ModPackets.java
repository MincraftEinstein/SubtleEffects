package einstein.subtle_effects.init;

import einstein.subtle_effects.networking.clientbound.*;

import static einstein.subtle_effects.platform.Services.NETWORK;

public class ModPackets {

    public static void init() {
        NETWORK.registerToClient(ClientBoundSpawnSnoreParticlePacket.ID, ClientBoundSpawnSnoreParticlePacket.class, ClientBoundSpawnSnoreParticlePacket::decode);
        NETWORK.registerToClient(ClientBoundEntityFellPacket.ID, ClientBoundEntityFellPacket.class, ClientBoundEntityFellPacket::decode);
        NETWORK.registerToClient(ClientBoundEntitySpawnSprintingDustCloudsPacket.ID, ClientBoundEntitySpawnSprintingDustCloudsPacket.class, ClientBoundEntitySpawnSprintingDustCloudsPacket::decode);
        NETWORK.registerToClient(ClientBoundBlockDestroyEffectsPacket.ID, ClientBoundBlockDestroyEffectsPacket.class, ClientBoundBlockDestroyEffectsPacket::decode);
        NETWORK.registerToClient(ClientBoundXPBottleEffectsPacket.ID, ClientBoundXPBottleEffectsPacket.class, ClientBoundXPBottleEffectsPacket::decode);
        NETWORK.registerToClient(ClientBoundFallingBlockLandPayload.ID, ClientBoundFallingBlockLandPayload.class, ClientBoundFallingBlockLandPayload::decode);
        NETWORK.registerToClient(ClientBoundCompostItemPayload.ID, ClientBoundCompostItemPayload.class, ClientBoundCompostItemPayload::decode);
        NETWORK.registerToClient(ClientBoundStonecutterUsedPayload.ID, ClientBoundStonecutterUsedPayload.class, ClientBoundStonecutterUsedPayload::decode);
        NETWORK.registerToClient(ClientBoundVillagerWorkPacket.ID, ClientBoundVillagerWorkPacket.class, ClientBoundVillagerWorkPacket::decode);
        NETWORK.registerToClient(ClientBoundAnimalFedPacket.ID, ClientBoundAnimalFedPacket.class, ClientBoundAnimalFedPacket::decode);
        NETWORK.registerToClient(ClientBoundMooshroomShearedPacket.ID, ClientBoundMooshroomShearedPacket.class, ClientBoundMooshroomShearedPacket::decode);
        NETWORK.registerToClient(ClientBoundSheepShearPayload.ID, ClientBoundSheepShearPayload.class, ClientBoundSheepShearPayload::decode);
        NETWORK.registerToClient(ClientBoundDrankPotionPayload.ID, ClientBoundDrankPotionPayload.class, ClientBoundDrankPotionPayload::decode);
        NETWORK.registerToClient(ClientBoundDispenseBucketPayload.ID, ClientBoundDispenseBucketPayload.class, ClientBoundDispenseBucketPayload::decode);
    }
}
