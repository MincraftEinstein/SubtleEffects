package einstein.ambient_sleep.init;

import commonnetwork.api.Network;
import einstein.ambient_sleep.networking.clientbound.ClientBoundSpawnSnoreParticlePacket;

public class ModPackets {

    public static void init() {
        Network.registerPacket(ClientBoundSpawnSnoreParticlePacket.CHANNEL, ClientBoundSpawnSnoreParticlePacket.class, ClientBoundSpawnSnoreParticlePacket::encode, ClientBoundSpawnSnoreParticlePacket::decode, ClientBoundSpawnSnoreParticlePacket::handle);
    }
}
