package einstein.subtle_effects.init;

import einstein.subtle_effects.networking.ClientPayloadHandlers;
import einstein.subtle_effects.networking.clientbound.*;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ModPackets {

    public static void init() {
        registerToClient(ClientBoundSpawnSnoreParticlePayload.ID, ClientBoundSpawnSnoreParticlePayload.class, ClientBoundSpawnSnoreParticlePayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundEntityFellPayload.ID, ClientBoundEntityFellPayload.class, ClientBoundEntityFellPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundEntitySpawnSprintingDustCloudsPayload.ID, ClientBoundEntitySpawnSprintingDustCloudsPayload.class, ClientBoundEntitySpawnSprintingDustCloudsPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundBlockDestroyEffectsPayload.ID, ClientBoundBlockDestroyEffectsPayload.class, ClientBoundBlockDestroyEffectsPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundXPBottleEffectsPayload.ID, ClientBoundXPBottleEffectsPayload.class, ClientBoundXPBottleEffectsPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundFallingBlockLandPayload.ID, ClientBoundFallingBlockLandPayload.class, ClientBoundFallingBlockLandPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundCompostItemPayload.ID, ClientBoundCompostItemPayload.class, ClientBoundCompostItemPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundStonecutterUsedPayload.ID, ClientBoundStonecutterUsedPayload.class, ClientBoundStonecutterUsedPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundVillagerWorkPayload.ID, ClientBoundVillagerWorkPayload.class, ClientBoundVillagerWorkPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundAnimalFedPayload.ID, ClientBoundAnimalFedPayload.class, ClientBoundAnimalFedPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundMooshroomShearedPayload.ID, ClientBoundMooshroomShearedPayload.class, ClientBoundMooshroomShearedPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundSheepShearPayload.ID, ClientBoundSheepShearPayload.class, ClientBoundSheepShearPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundDrankPotionPayload.ID, ClientBoundDrankPotionPayload.class, ClientBoundDrankPotionPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundDispenseBucketPayload.ID, ClientBoundDispenseBucketPayload.class, ClientBoundDispenseBucketPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundEntityLandInFluidPayload.ID, ClientBoundEntityLandInFluidPayload.class, ClientBoundEntityLandInFluidPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundMobSpawnerSpawnPayload.ID, ClientBoundMobSpawnerSpawnPayload.class, ClientBoundMobSpawnerSpawnPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundEntityDamagedPayload.ID, ClientBoundEntityDamagedPayload.class, ClientBoundEntityDamagedPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundChargedCreeperExplosionPayload.ID, ClientBoundChargedCreeperExplosionPayload.class, ClientBoundChargedCreeperExplosionPayload::read, ClientPayloadHandlers::handle);
        registerToClient(ClientBoundItemEnchantedPayload.ID, ClientBoundItemEnchantedPayload.class, ClientBoundItemEnchantedPayload::read, ClientPayloadHandlers::handle);
    }

    private static <T extends FzzyPayload> void registerToClient(ResourceLocation id, Class<T> clazz, Function<FriendlyByteBuf, T> reader, BiConsumer<Level, T> handler) {
        ConfigApiJava.network().registerLenientS2C(id, clazz, reader, (payload, context) -> context.execute(() -> handler.accept(context.player().level(), payload)));
    }
}
