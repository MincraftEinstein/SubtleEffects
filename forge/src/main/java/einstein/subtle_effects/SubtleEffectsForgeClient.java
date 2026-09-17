package einstein.subtle_effects;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import einstein.subtle_effects.client.renderer.DebugScreenOverlayRenderer;
import einstein.subtle_effects.client.renderer.ParticleBoundingBoxesRenderer;
import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.init.ModRenderTypes;
import einstein.subtle_effects.platform.ForgeParticleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.io.IOException;
import java.util.function.Consumer;

import static einstein.subtle_effects.SubtleEffectsClient.*;

public class SubtleEffectsForgeClient {

    public SubtleEffectsForgeClient(IEventBus modEventBus) {
        clientSetup();

        try {
            Class.forName("net.optifine.Config");
            SubtleEffects.LOGGER.warn("Optifine detected! Developer support for optifine related issues is little to none");
        }
        catch (ClassNotFoundException e) {
        }

        modEventBus.addListener((RegisterParticleProvidersEvent event) ->
                ForgeParticleHelper.PARTICLE_PROVIDERS.forEach(consumer -> consumer.accept(event))
        );
        modEventBus.addListener((AddPackFindersEvent event) -> {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                event.addRepositorySource(new BCWPSource());
            }
        });
        modEventBus.addListener((RegisterClientReloadListenersEvent event) -> registerReloadListeners().forEach(event::registerReloadListener));
        modEventBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) ->
                registerModelLayers().forEach(event::registerLayerDefinition)
        );
        modEventBus.addListener((EntityRenderersEvent.AddLayers event) -> {
            for (String model : event.getSkins()) {
                EntityRenderer<?> renderer = event.getSkin(model);

                if (renderer instanceof PlayerRenderer playerRenderer) {
                    registerPlayerRenderLayers(playerRenderer, event.getContext())
                            .forEach(playerRenderer::addLayer);
                }
            }
        });
        modEventBus.addListener((RegisterShadersEvent event) -> {
                    try {
                        event.registerShader(new ShaderInstance(event.getResourceProvider(), ModRenderTypes.RENDERTYPE_ENTITY_PARTICLE_TRANSLUCENT_SHADER_ID, DefaultVertexFormat.NEW_ENTITY),
                                shaderInstance -> ModRenderTypes.RENDERTYPE_ENTITY_PARTICLE_TRANSLUCENT_SHADER = shaderInstance);
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        modEventBus.addListener((RegisterGuiOverlaysEvent event) ->
                event.registerBelowAll("debug_overlay", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> DebugScreenOverlayRenderer.render(guiGraphics)));
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) {
                Minecraft minecraft = Minecraft.getInstance();
                clientTick(minecraft, minecraft.level);
            }
        });
        MinecraftForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) ->
                registerClientCommands(event.getDispatcher(), event.getBuildContext()));
        MinecraftForge.EVENT_BUS.addListener((RenderLevelStageEvent event) -> {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                ParticleBoundingBoxesRenderer.render(event.getPoseStack(), event.getCamera());
            }
        });
    }

    public static class BCWPSource implements RepositorySource {

        @Override
        public void loadPacks(Consumer<Pack> consumer) {
            IModInfo info = ModList.get().getModContainerById(SubtleEffects.MOD_ID).orElseThrow().getModInfo();

            consumer.accept(Pack.readMetaAndCreate(
                    "mod/" + BCWPPackManager.PACK_LOCATION.get().toString(), BCWPPackManager.PACK_NAME, false, path ->
                            new PathPackResources(path, info.getOwningFile().getFile().findResource(BCWPPackManager.PACK_LOCATION.get().getPath()), true),
                    PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN
            ));
        }
    }
}
