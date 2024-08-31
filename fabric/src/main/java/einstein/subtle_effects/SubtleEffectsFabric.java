package einstein.subtle_effects;

import einstein.subtle_effects.init.ModConfigs;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.neoforged.fml.config.ModConfig;

import static einstein.subtle_effects.SubtleEffects.MOD_ID;

public class SubtleEffectsFabric implements ModInitializer, ClientModInitializer {
    
    @Override
    public void onInitialize() {
        SubtleEffects.init();
        NeoForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.CLIENT, ModConfigs.SPEC);
        NeoForgeModConfigEvents.reloading(MOD_ID).register(config -> {
            if (config.getModId().equals(MOD_ID)) {
                SubtleEffects.configReloaded();
            }
        });
    }

    @Override
    public void onInitializeClient() {
        SubtleEffects.clientSetup();
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> SubtleEffects.clientTick(minecraft, minecraft.level));
    }
}
