package einstein.subtle_effects;

import einstein.subtle_effects.data.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class SubtleEffectsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SubtleEffectsClient.clientSetup();
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> SubtleEffectsClient.clientTick(minecraft, minecraft.level));
        ClientCommandRegistrationCallback.EVENT.register(SubtleEffectsClient::registerClientCommands);
        ResourceManagerHelper.registerBuiltinResourcePack(BCWPPackManager.PACK_LOCATION.get(), FabricLoader.getInstance().getModContainer(SubtleEffects.MOD_ID).orElseThrow(), BCWPPackManager.PACK_NAME, ResourcePackActivationType.NORMAL);
        ResourceManagerHelper helper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
        addReloadListener(helper, new SparkProviderReloadListener());
        addReloadListener(helper, new MobSkullShaderReloadListener());
        addReloadListener(helper, new BCWPPackManager());
    }

    private static <T extends PreparableReloadListener & NamedReloadListener> void addReloadListener(ResourceManagerHelper helper, T listener) {
        helper.registerReloadListener(new FabricReloadListenerWrapper<>(listener));
    }
}
