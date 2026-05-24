package einstein.subtle_effects;

import net.neoforged.fml.common.Mod;

import static einstein.subtle_effects.SubtleEffects.MOD_ID;

@Mod(MOD_ID)
public class SubtleEffectsNeoForge {

    public SubtleEffectsNeoForge() {
        SubtleEffects.init();
    }
}
