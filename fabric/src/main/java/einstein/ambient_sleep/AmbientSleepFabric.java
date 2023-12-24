package einstein.ambient_sleep;

import net.fabricmc.api.ModInitializer;

public class AmbientSleepFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        AmbientSleep.init();
    }
}
