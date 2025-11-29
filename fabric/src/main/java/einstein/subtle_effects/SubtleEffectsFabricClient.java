package einstein.subtle_effects;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import einstein.subtle_effects.data.*;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.init.ModRenderTypes;
import einstein.subtle_effects.platform.FabricNetworkHelper;
import einstein.subtle_effects.platform.services.NetworkHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

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
        SubtleEffectsClient.registerModelLayers().forEach((modelLayerLocation, layerDefinitionSupplier) ->
                EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinitionSupplier::get)
        );
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, registrationHelper, context) -> {
            if (renderer instanceof PlayerRenderer playerRenderer) {
                SubtleEffectsClient.registerPlayerRenderLayers(playerRenderer, context).forEach(registrationHelper::register);
            }
        });
        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(ModRenderTypes.RENDERTYPE_ENTITY_PARTICLE_TRANSLUCENT_SHADER_ID, DefaultVertexFormat.NEW_ENTITY, shaderInstance -> ModRenderTypes.RENDERTYPE_ENTITY_PARTICLE_TRANSLUCENT_SHADER = shaderInstance));
    }

    private static <T extends PreparableReloadListener & NamedReloadListener> void addReloadListener(ResourceManagerHelper helper, T listener) {
        helper.registerReloadListener(new FabricReloadListenerWrapper<>(listener));
    }
}
