package einstein.ambient_sleep;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.platform.ForgeRegistryHelper;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AmbientSleep.MOD_ID)
public class AmbientSleepForge {

    public AmbientSleepForge() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        AmbientSleep.init();
        ForgeRegistryHelper.PARTICLE_TYPES.register(modEventBus);
        ForgeRegistryHelper.SOUND_EVENTS.register(modEventBus);
        modEventBus.addListener((RegisterParticleProvidersEvent event) ->
                ForgeRegistryHelper.PARTICLE_PROVIDERS.forEach((particle, provider) ->
                        event.registerSpriteSet(particle.get(), provider::apply)
                )
        );
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModConfigs.SPEC);
    }
}