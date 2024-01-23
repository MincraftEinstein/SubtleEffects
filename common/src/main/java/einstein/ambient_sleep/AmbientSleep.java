package einstein.ambient_sleep;

import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.init.ModSounds;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmbientSleep {

    public static final String MOD_ID = "ambient_sleep";
    public static final String MOD_NAME = "Ambient Sleep";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        ModSounds.init();
        ModParticles.init();
    }

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}