package einstein.subtle_effects;

import einstein.subtle_effects.client.renderer.DebugScreenOverlayRenderer;
import einstein.subtle_effects.client.renderer.ParticleBoundingBoxesRenderer;
import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.platform.NeoForgeParticleHelper;
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

import static einstein.subtle_effects.SubtleEffectsClient.*;

@Mod(value = SubtleEffects.MOD_ID, dist = Dist.CLIENT)
public class SubtleEffectsNeoForgeClient {

    public SubtleEffectsNeoForgeClient(IEventBus modEventBus) {
        clientSetup();
        modEventBus.addListener((RegisterParticleProvidersEvent event) ->
                NeoForgeParticleHelper.PARTICLE_PROVIDERS.forEach(consumer -> consumer.accept(event))
        );
        modEventBus.addListener((AddPackFindersEvent event) -> {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                event.addPackFinders(BCWPPackManager.PACK_LOCATION.get(), PackType.CLIENT_RESOURCES,
                        BCWPPackManager.PACK_NAME, PackSource.BUILT_IN, false, Pack.Position.TOP);
            }
        });
        modEventBus.addListener((RegisterClientReloadListenersEvent event) ->
                registerReloadListeners().forEach(event::registerReloadListener)
        );
        modEventBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) ->
                registerModelLayers().forEach(event::registerLayerDefinition)
        );
        modEventBus.addListener((EntityRenderersEvent.AddLayers event) -> {
            for (PlayerSkin.Model model : event.getSkins()) {
                EntityRenderer<?> renderer = event.getSkin(model);

                if (renderer instanceof PlayerRenderer playerRenderer) {
                    registerPlayerRenderLayers(playerRenderer, event.getContext())
                            .forEach(playerRenderer::addLayer);
                }
            }
        });
        modEventBus.addListener((RegisterGuiLayersEvent event) ->
                event.registerBelowAll(SubtleEffects.loc("debug_overlay"), DebugScreenOverlayRenderer::render)
        );
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> {
            Minecraft minecraft = Minecraft.getInstance();
            clientTick(minecraft, minecraft.level);
        });
        NeoForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) ->
                registerClientCommands(event.getDispatcher(), event.getBuildContext())
        );
        NeoForge.EVENT_BUS.addListener((RenderLevelStageEvent event) -> {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                ParticleBoundingBoxesRenderer.render(event.getPoseStack(), event.getCamera());
            }
        });
    }
}
