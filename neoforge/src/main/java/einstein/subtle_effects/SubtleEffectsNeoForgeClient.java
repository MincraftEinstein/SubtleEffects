package einstein.subtle_effects;

import einstein.subtle_effects.client.renderer.DebugScreenOverlayRenderer;
import einstein.subtle_effects.client.renderer.ParticleBoundingBoxesRenderer;
import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.data.NamedReloadListener;
import einstein.subtle_effects.data.splash_types.SplashTypeReloadListener;
import einstein.subtle_effects.platform.NeoForgeParticleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.player.PlayerModelType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.resources.VanillaClientListeners;
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
        modEventBus.addListener((RegisterParticleGroupsEvent event) ->
                NeoForgeParticleHelper.PARTICLE_GROUPS.forEach(consumer -> consumer.accept(event))
        );
        modEventBus.addListener((AddPackFindersEvent event) -> {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                event.addPackFinders(BCWPPackManager.PACK_LOCATION.get(), PackType.CLIENT_RESOURCES,
                        BCWPPackManager.PACK_NAME, PackSource.BUILT_IN, false, Pack.Position.TOP);
            }
        });
        modEventBus.addListener((AddClientReloadListenersEvent event) -> {
            registerReloadListeners().forEach(listener -> addReloadListener(event, listener));
            addReloadListener(event, new SplashTypeReloadListener());
            event.addDependency(SplashTypeReloadListener.ID, VanillaClientListeners.FIRST);
        });
        modEventBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) ->
                registerModelLayers().forEach(event::registerLayerDefinition)
        );
        modEventBus.addListener((EntityRenderersEvent.AddLayers event) -> {
            for (PlayerModelType model : event.getSkins()) {
                AvatarRenderer<AbstractClientPlayer> renderer = event.getPlayerRenderer(model);
                if (renderer == null) {
                    continue;
                }

                if (renderer instanceof AvatarRenderer<?> avatarRenderer) {
                    registerPlayerRenderLayers(avatarRenderer, event.getContext())
                            .forEach(avatarRenderer::addLayer);
                }
            }
        });
        modEventBus.addListener((RegisterGuiLayersEvent event) ->
                event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, SubtleEffects.loc("debug_overlay"), DebugScreenOverlayRenderer::extract)
        );
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> {
            Minecraft minecraft = Minecraft.getInstance();
            clientTick(minecraft, minecraft.level);
        });
        NeoForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) ->
                registerClientCommands(event.getDispatcher(), event.getBuildContext()));
        NeoForge.EVENT_BUS.addListener((ExtractLevelRenderStateEvent event) ->
                ParticleBoundingBoxesRenderer.extract(event.getRenderState(), event.getRenderState().cameraRenderState));
        NeoForge.EVENT_BUS.addListener((RenderLevelStageEvent.AfterTranslucentParticles event) ->
                ParticleBoundingBoxesRenderer.render(event.getLevelRenderState()));
    }

    private static <T extends PreparableReloadListener & NamedReloadListener> void addReloadListener(AddClientReloadListenersEvent event, T listener) {
        event.addListener(listener.getId(), listener);
    }
}
