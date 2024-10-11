package einstein.subtle_effects;

import einstein.subtle_effects.platform.ForgeNetworkHelper;
import einstein.subtle_effects.platform.ForgeRegistryHelper;
import einstein.subtle_effects.platform.services.NetworkHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

import static einstein.subtle_effects.SubtleEffects.MOD_ID;

@Mod(MOD_ID)
public class SubtleEffectsForge {

    public SubtleEffectsForge(IEventBus modEventBus) {
        SubtleEffects.init();
        ForgeNetworkHelper.init(NetworkHelper.Direction.TO_CLIENT);
        ForgeRegistryHelper.PARTICLE_TYPES.register(modEventBus);
        ForgeRegistryHelper.SOUND_EVENTS.register(modEventBus);
    }
}