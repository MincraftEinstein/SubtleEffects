package einstein.subtle_effects.init;

import commonnetwork.CommonNetworkMod;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntityFellPacket;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntitySpawnSprintingDustCloudsPacket;
import einstein.subtle_effects.networking.clientbound.ClientBoundSpawnSnoreParticlePacket;

public class ModPackets {

    public static void init() {
        CommonNetworkMod.registerPacket(ClientBoundSpawnSnoreParticlePacket.TYPE, ClientBoundSpawnSnoreParticlePacket.class, ClientBoundSpawnSnoreParticlePacket.STREAM_CODEC, ClientBoundSpawnSnoreParticlePacket::handle);
        CommonNetworkMod.registerPacket(ClientBoundEntityFellPacket.TYPE, ClientBoundEntityFellPacket.class, ClientBoundEntityFellPacket.STREAM_CODEC, ClientBoundEntityFellPacket::handle);
        CommonNetworkMod.registerPacket(ClientBoundEntitySpawnSprintingDustCloudsPacket.TYPE, ClientBoundEntitySpawnSprintingDustCloudsPacket.class, ClientBoundEntitySpawnSprintingDustCloudsPacket.STREAM_CODEC, ClientBoundEntitySpawnSprintingDustCloudsPacket::handle);
    }
}
