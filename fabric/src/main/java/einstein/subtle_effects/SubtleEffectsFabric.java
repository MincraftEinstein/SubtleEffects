package einstein.subtle_effects;

import einstein.subtle_effects.init.ModPackets;
import einstein.subtle_effects.init.ModParticles;
import net.fabricmc.api.ModInitializer;

public class SubtleEffectsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        SubtleEffects.init();
        ModParticles.init();
        ModPackets.init();
    }
}
