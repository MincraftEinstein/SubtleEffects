package einstein.subtle_effects;

import einstein.subtle_effects.init.ModConfigs;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraftforge.fml.config.ModConfig;

public class SubtleEffectsFabric implements ModInitializer, ClientModInitializer {
    
    @Override
    public void onInitialize() {
        SubtleEffects.init();
        ForgeConfigRegistry.INSTANCE.register(SubtleEffects.MOD_ID, ModConfig.Type.CLIENT, ModConfigs.SPEC);
        ModConfigEvents.reloading(SubtleEffects.MOD_ID).register(SubtleEffects::configReloaded);
    }

    @Override
    public void onInitializeClient() {
        SubtleEffects.clientSetup();
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> SubtleEffects.levelTick(minecraft, minecraft.level));
    }
}
