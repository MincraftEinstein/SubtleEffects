package einstein.subtle_effects;

import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.data.NamedReloadListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class SubtleEffectsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SubtleEffectsClient.clientSetup();
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> SubtleEffectsClient.clientTick(minecraft, minecraft.level));
        ClientCommandRegistrationCallback.EVENT.register(SubtleEffectsClient::registerClientCommands);
        ResourceLoader.registerBuiltinPack(BCWPPackManager.PACK_LOCATION.get(), FabricLoader.getInstance().getModContainer(SubtleEffects.MOD_ID).orElseThrow(), BCWPPackManager.PACK_NAME, PackActivationType.NORMAL);
        ResourceLoader loader = ResourceLoader.get(PackType.CLIENT_RESOURCES);
        SubtleEffectsClient.registerReloadListeners().forEach(listener -> addReloadListener(loader, listener));
        SubtleEffectsClient.registerModelLayers().forEach((modelLayerLocation, layerDefinitionSupplier) ->
                EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinitionSupplier::get)
        );
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, registrationHelper, context) -> {
            if (renderer instanceof AvatarRenderer<?> playerRenderer) {
                SubtleEffectsClient.registerPlayerRenderLayers(playerRenderer, context).forEach(registrationHelper::register);
            }
        });
    }

    private static <T extends PreparableReloadListener & NamedReloadListener> void addReloadListener(ResourceLoader loader, T listener) {
        loader.registerReloader(listener.getId(), listener);
    }
}
