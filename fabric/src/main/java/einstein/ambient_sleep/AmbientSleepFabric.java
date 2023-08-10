package einstein.ambient_sleep;

import einstein.ambient_sleep.client.particle.SnoringParticle;
import einstein.ambient_sleep.init.ModInit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class AmbientSleepFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        AmbientSleep.init();
        ParticleFactoryRegistry.getInstance().register(ModInit.SNORING_PARTICLE.get(), SnoringParticle.Provider::new);
    }
}
