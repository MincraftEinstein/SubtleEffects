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
    }

    public static void initClientHandlers() {
        NETWORK.registerClientHandler(ClientBoundSpawnSnoreParticlePayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundEntityFellPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundEntitySpawnSprintingDustCloudsPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundBlockDestroyEffectsPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundXPBottleEffectsPayload.TYPE, ClientPacketHandlers::handle);
        NETWORK.registerClientHandler(ClientBoundFallingBlockLandPayload.TYPE, ClientPacketHandlers::handle);
    }
}
