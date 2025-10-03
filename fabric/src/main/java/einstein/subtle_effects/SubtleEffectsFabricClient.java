package einstein.subtle_effects;

import einstein.subtle_effects.client.model.entity.EinsteinSolarSystemModel;
import einstein.subtle_effects.client.model.entity.PartyHatModel;
import einstein.subtle_effects.client.renderer.entity.EinsteinSolarSystemLayer;
import einstein.subtle_effects.client.renderer.entity.PartyHatLayer;
import einstein.subtle_effects.data.*;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.platform.FabricNetworkHelper;
import einstein.subtle_effects.platform.services.NetworkHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.EntityType;

public class SubtleEffectsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SubtleEffectsClient.clientSetup();
        ModParticles.init();
        FabricNetworkHelper.init(NetworkHelper.Direction.TO_CLIENT);
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> SubtleEffectsClient.clientTick(minecraft, minecraft.level));
        ClientCommandRegistrationCallback.EVENT.register(SubtleEffectsClient::registerClientCommands);
        ResourceManagerHelper.registerBuiltinResourcePack(BCWPPackManager.PACK_LOCATION.get(), FabricLoader.getInstance().getModContainer(SubtleEffects.MOD_ID).orElseThrow(), BCWPPackManager.PACK_NAME, ResourcePackActivationType.NORMAL);
        ResourceManagerHelper helper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
        addReloadListener(helper, new SparkProviderReloadListener());
        addReloadListener(helper, new MobSkullShaderReloadListener());
        addReloadListener(helper, new BCWPPackManager());
        EntityModelLayerRegistry.registerModelLayer(EinsteinSolarSystemModel.MODEL_LAYER, EinsteinSolarSystemModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(PartyHatModel.MODEL_LAYER, PartyHatModel::createLayer);
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, registrationHelper, context) -> {
            if (type.equals(EntityType.PLAYER)) {
                registrationHelper.register(new EinsteinSolarSystemLayer<>(renderer, context));

                if (PartyHatLayer.isModAnniversary()) {
                    registrationHelper.register(new PartyHatLayer<>(renderer, context));
                }
            }
        });
    }

    private static <T extends PreparableReloadListener & NamedReloadListener> void addReloadListener(ResourceManagerHelper helper, T listener) {
        helper.registerReloadListener(new FabricReloadListenerWrapper<>(listener));
    }
}
