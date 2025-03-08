package einstein.subtle_effects;

import einstein.subtle_effects.configs.ModServerConfigs;
import einstein.subtle_effects.init.ModPackets;
import einstein.subtle_effects.init.ModSounds;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubtleEffects {

    public static final String MOD_ID = "subtle_effects";
    public static final String MOD_NAME = "Subtle Effects";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static final ModServerConfigs SERVER_CONFIGS = ConfigApiJava.registerAndLoadConfig(ModServerConfigs::new);

    public static void init() {
        ModSounds.init();
        ModPackets.init();
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}