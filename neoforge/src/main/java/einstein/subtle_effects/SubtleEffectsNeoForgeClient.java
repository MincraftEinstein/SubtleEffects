package einstein.subtle_effects;

import einstein.subtle_effects.client.renderer.DebugRenderers;
import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.platform.NeoForgeParticleHelper;
import einstein.subtle_effects.platform.NeoForgeRegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@Mod(value = SubtleEffects.MOD_ID, dist = Dist.CLIENT)
public class SubtleEffectsNeoForgeClient {

    public SubtleEffectsNeoForgeClient(IEventBus modEventBus) {
        SubtleEffectsClient.clientSetup();
        modEventBus.addListener((RegisterParticleProvidersEvent event) ->
                NeoForgeParticleHelper.PARTICLE_PROVIDERS.forEach(
                        consumer -> consumer.accept(event))
        );
        modEventBus.addListener((AddPackFindersEvent event) -> {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                event.addPackFinders(BCWPPackManager.PACK_LOCATION.get(), PackType.CLIENT_RESOURCES,
                        BCWPPackManager.PACK_NAME, PackSource.BUILT_IN, false, Pack.Position.TOP);
            }
        });
        modEventBus.addListener((RegisterClientReloadListenersEvent event) ->
                SubtleEffectsClient.registerReloadListeners().forEach(event::registerReloadListener)
        );
        modEventBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) ->
                SubtleEffectsClient.registerModelLayers().forEach(event::registerLayerDefinition)
        );
        modEventBus.addListener((EntityRenderersEvent.AddLayers event) -> {
            for (PlayerSkin.Model model : event.getSkins()) {
                EntityRenderer<?> renderer = event.getSkin(model);

                if (renderer instanceof PlayerRenderer playerRenderer) {
                    SubtleEffectsClient.registerPlayerRenderLayers(playerRenderer, event.getContext())
                            .forEach(playerRenderer::addLayer);
                }
            }
        });
        modEventBus.addListener((RegisterGuiLayersEvent event) ->
                event.registerBelowAll(SubtleEffects.loc("debug_overlay"), DebugRenderers::renderDebugScreenOverlay)
        );
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> {
            Minecraft minecraft = Minecraft.getInstance();
            SubtleEffectsClient.clientTick(minecraft, minecraft.level);
        });
        NeoForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) ->
                SubtleEffectsClient.registerClientCommands(event.getDispatcher(), event.getBuildContext()));
        NeoForge.EVENT_BUS.addListener((RenderLevelStageEvent event) -> {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                DebugRenderers.renderParticleBoundingBoxes(event.getPoseStack(), event.getCamera());
            }
        });
    }
}
