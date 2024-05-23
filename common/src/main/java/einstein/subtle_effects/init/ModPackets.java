package einstein.subtle_effects.init;

import commonnetwork.api.Network;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntityFellPacket;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntitySpawnSprintingDustCloudsPacket;
import einstein.subtle_effects.networking.clientbound.ClientBoundSpawnSnoreParticlePacket;

public class ModPackets {

    public static void init() {
        Network.registerPacket(ClientBoundSpawnSnoreParticlePacket.CHANNEL, ClientBoundSpawnSnoreParticlePacket.class, ClientBoundSpawnSnoreParticlePacket::encode, ClientBoundSpawnSnoreParticlePacket::decode, ClientBoundSpawnSnoreParticlePacket::handle);
        Network.registerPacket(ClientBoundEntityFellPacket.CHANNEL, ClientBoundEntityFellPacket.class, ClientBoundEntityFellPacket::encode, ClientBoundEntityFellPacket::decode, ClientBoundEntityFellPacket::handle);
        Network.registerPacket(ClientBoundEntitySpawnSprintingDustCloudsPacket.CHANNEL, ClientBoundEntitySpawnSprintingDustCloudsPacket.class, ClientBoundEntitySpawnSprintingDustCloudsPacket::encode, ClientBoundEntitySpawnSprintingDustCloudsPacket::decode, ClientBoundEntitySpawnSprintingDustCloudsPacket::handle);
    }
}
