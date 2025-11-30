package einstein.subtle_effects;

import einstein.subtle_effects.init.ModPayloads;
import einstein.subtle_effects.init.ModSounds;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubtleEffects {

    public static final String MOD_ID = "subtle_effects";
    public static final String MOD_NAME = "Subtle Effects";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        ModSounds.init();
        ModPayloads.init();
    }

    public static Identifier loc(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}