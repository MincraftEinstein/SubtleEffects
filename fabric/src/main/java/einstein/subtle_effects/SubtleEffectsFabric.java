package einstein.subtle_effects;

import einstein.subtle_effects.platform.FabricNetworkHelper;
import einstein.subtle_effects.platform.services.NetworkHelper;
import net.fabricmc.api.ModInitializer;

public class SubtleEffectsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        SubtleEffects.init();
        FabricNetworkHelper.init(NetworkHelper.Direction.TO_SERVER);
    }
}
