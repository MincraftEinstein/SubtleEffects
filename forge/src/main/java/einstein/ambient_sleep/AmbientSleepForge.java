package einstein.ambient_sleep;

import einstein.ambient_sleep.client.particle.SnoringParticle;
import einstein.ambient_sleep.init.ModInit;
import einstein.ambient_sleep.platform.ForgeRegistryHelper;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AmbientSleep.MOD_ID)
public class AmbientSleepForge {
    
    public AmbientSleepForge() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        AmbientSleep.init();
        ForgeRegistryHelper.PARTICLE_TYPES.register(modEventBus);
        modEventBus.addListener((RegisterParticleProvidersEvent event) -> {
            event.registerSpriteSet(ModInit.SNORING.get(), SnoringParticle.Provider::new);
        });
    }
}