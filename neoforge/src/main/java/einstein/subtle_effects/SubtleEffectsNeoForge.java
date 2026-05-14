package einstein.subtle_effects;

import einstein.subtle_effects.platform.NeoForgeRegistryHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import static einstein.subtle_effects.SubtleEffects.MOD_ID;

@Mod(MOD_ID)
public class SubtleEffectsNeoForge {

    public SubtleEffectsNeoForge(IEventBus modEventBus) {
        SubtleEffects.init();
        NeoForgeRegistryHelper.PARTICLE_TYPES.register(modEventBus);
        NeoForgeRegistryHelper.SOUND_EVENTS.register(modEventBus);
    }
}
