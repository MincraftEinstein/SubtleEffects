package einstein.subtle_effects;

import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.data.FabricReloadListenerWrapper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.server.packs.PackType;

public class SubtleEffectsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SubtleEffectsClient.clientSetup();
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> SubtleEffectsClient.clientTick(minecraft, minecraft.level));
        ClientCommandRegistrationCallback.EVENT.register(SubtleEffectsClient::registerClientCommands);
        ResourceManagerHelper.registerBuiltinResourcePack(BCWPPackManager.PACK_LOCATION.get(), FabricLoader.getInstance().getModContainer(SubtleEffects.MOD_ID).orElseThrow(), BCWPPackManager.PACK_NAME, ResourcePackActivationType.NORMAL);
        ResourceManagerHelper helper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
        SubtleEffectsClient.registerReloadListeners().forEach(listener ->
                helper.registerReloadListener(new FabricReloadListenerWrapper<>(listener))
        );
        SubtleEffectsClient.registerModelLayers().forEach((modelLayerLocation, layerDefinitionSupplier) ->
                EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinitionSupplier::get)
        );
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, registrationHelper, context) -> {
            if (renderer instanceof PlayerRenderer playerRenderer) {
                SubtleEffectsClient.registerPlayerRenderLayers(playerRenderer, context).forEach(registrationHelper::register);
            }
        });
    }
}
