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
    }
}
