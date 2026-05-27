package einstein.subtle_effects;

import einstein.subtle_effects.client.renderer.ParticleBoundingBoxesRenderer;
import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.data.NamedReloadListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
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
                ModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinitionSupplier::get)
        );
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((_, renderer, registrationHelper, context) -> {
            if (renderer instanceof AvatarRenderer<?> playerRenderer) {
                SubtleEffectsClient.registerPlayerRenderLayers(playerRenderer, context).forEach(registrationHelper::register);
            }
        });

        LevelRenderEvents.BEFORE_GIZMOS.register((LevelRenderContext context)-> {
            var levelState = context.levelState();
            ParticleBoundingBoxesRenderer.extractParticleBoundingBoxes(levelState, levelState.cameraRenderState);
            ParticleBoundingBoxesRenderer.renderParticleBoundingBoxes(levelState);
        });
    }

    private static <T extends PreparableReloadListener & NamedReloadListener> void addReloadListener(ResourceLoader loader, T listener) {
        loader.registerReloadListener(listener.getId(), listener);
    }
}
