package einstein.ambient_sleep;

import einstein.ambient_sleep.init.ModConfigs;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraftforge.fml.config.ModConfig;

public class AmbientSleepFabric implements ModInitializer, ClientModInitializer {
    
    @Override
    public void onInitialize() {
        AmbientSleep.init();
        ForgeConfigRegistry.INSTANCE.register(AmbientSleep.MOD_ID, ModConfig.Type.CLIENT, ModConfigs.SPEC);
        ModConfigEvents.reloading(AmbientSleep.MOD_ID).register(AmbientSleep::configReloaded);
    }

    @Override
    public void onInitializeClient() {
        AmbientSleep.clientSetup();
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> AmbientSleep.levelTick(minecraft, minecraft.level));
    }
}
