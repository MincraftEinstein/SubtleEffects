package einstein.subtle_effects.init;

import einstein.subtle_effects.networking.clientbound.*;

import static einstein.subtle_effects.platform.Services.NETWORK;

public class ModPackets {

    public static void init() {
        NETWORK.registerToClient(ClientBoundSpawnSnoreParticlePacket.TYPE, ClientBoundSpawnSnoreParticlePacket.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundEntityFellPacket.TYPE, ClientBoundEntityFellPacket.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundEntitySpawnSprintingDustCloudsPacket.TYPE, ClientBoundEntitySpawnSprintingDustCloudsPacket.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundBlockDestroyEffectsPacket.TYPE, ClientBoundBlockDestroyEffectsPacket.STREAM_CODEC);
        NETWORK.registerToClient(ClientBoundXPBottleEffectsPacket.TYPE, ClientBoundXPBottleEffectsPacket.STREAM_CODEC);
    }

    public static void initClientHandlers() {
        NETWORK.registerClientHandler(ClientBoundSpawnSnoreParticlePacket.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundEntityFellPacket.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundEntitySpawnSprintingDustCloudsPacket.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundBlockDestroyEffectsPacket.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundXPBottleEffectsPacket.TYPE, ClientPacketHandlers::handle);
    }
}
