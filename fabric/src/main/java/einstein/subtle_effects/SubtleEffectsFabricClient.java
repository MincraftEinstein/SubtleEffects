package einstein.subtle_effects;

import einstein.subtle_effects.init.ModConfigs;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.neoforged.fml.config.ModConfig;

import static einstein.subtle_effects.SubtleEffects.MOD_ID;

public class SubtleEffectsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SubtleEffectsClient.clientSetup();
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> SubtleEffectsClient.clientTick(minecraft, minecraft.level));
        NeoForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.CLIENT, ModConfigs.SPEC);
        NeoForgeModConfigEvents.reloading(MOD_ID).register(config -> {
            if (config.getModId().equals(MOD_ID)) {
                SubtleEffectsClient.configReloaded();
            }
        });
    }
}
