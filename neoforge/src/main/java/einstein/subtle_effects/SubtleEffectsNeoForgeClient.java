package einstein.subtle_effects;

import einstein.subtle_effects.client.renderer.entity.EinsteinSolarSystemLayer;
import einstein.subtle_effects.client.model.entity.EinsteinSolarSystemModel;
import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.data.MobSkullShaderReloadListener;
import einstein.subtle_effects.data.SparkProviderReloadListener;
import einstein.subtle_effects.platform.NeoForgeRegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.util.function.Function;
import java.util.function.Supplier;

@Mod(value = SubtleEffects.MOD_ID, dist = Dist.CLIENT)
public class SubtleEffectsNeoForgeClient {

    public SubtleEffectsNeoForgeClient(IEventBus modEventBus) {
        SubtleEffectsClient.clientSetup();
        modEventBus.addListener((RegisterParticleProvidersEvent event) ->
                NeoForgeRegistryHelper.PARTICLE_PROVIDERS.forEach((particle, provider) -> registerParticle(event, particle, provider))
        );
        modEventBus.addListener((AddPackFindersEvent event) ->
                event.addPackFinders(BCWPPackManager.PACK_LOCATION.get(), PackType.CLIENT_RESOURCES,
                        BCWPPackManager.PACK_NAME, PackSource.BUILT_IN, false, Pack.Position.TOP));
        modEventBus.addListener((RegisterClientReloadListenersEvent event) -> {
            event.registerReloadListener(new SparkProviderReloadListener());
            event.registerReloadListener(new MobSkullShaderReloadListener());
            event.registerReloadListener(new BCWPPackManager());
        });
        modEventBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) ->
                event.registerLayerDefinition(EinsteinSolarSystemModel.MODEL_LAYER, EinsteinSolarSystemModel::createLayer));
        modEventBus.addListener((EntityRenderersEvent.AddLayers event) -> {
            for (PlayerSkin.Model model : event.getSkins()) {
                EntityRenderer<?> renderer = event.getSkin(model);

                if (renderer instanceof PlayerRenderer playerRenderer) {
                    playerRenderer.addLayer(new EinsteinSolarSystemLayer<>(playerRenderer, event.getContext()));
                }
            }
        });
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> {
            Minecraft minecraft = Minecraft.getInstance();
            SubtleEffectsClient.clientTick(minecraft, minecraft.level);
        });
        NeoForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) ->
                SubtleEffectsClient.registerClientCommands(event.getDispatcher(), event.getBuildContext()));
    }

    @SuppressWarnings("unchecked")
    private static <T extends ParticleType<V>, V extends ParticleOptions> void registerParticle(RegisterParticleProvidersEvent event, Supplier<? extends ParticleType<?>> particle, Function<SpriteSet, ? extends ParticleProvider<?>> provider) {
        Supplier<T> t = (Supplier<T>) particle;
        Function<SpriteSet, V> v = (Function<SpriteSet, V>) provider;
        event.registerSpriteSet(t.get(), sprites -> (ParticleProvider<V>) v.apply(sprites));
    }
}
