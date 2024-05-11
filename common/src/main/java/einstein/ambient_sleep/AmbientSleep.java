package einstein.ambient_sleep;

import einstein.ambient_sleep.init.*;
import einstein.ambient_sleep.util.ParticleManager;
import einstein.ambient_sleep.util.ShaderManager;
import einstein.ambient_sleep.tickers.TickerManager;
import einstein.ambient_sleep.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmbientSleep {

    public static final String MOD_ID = "ambient_sleep";
    public static final String MOD_NAME = "Ambient Sleep";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        ModSounds.init();
        ModParticles.init();
        ModPackets.init();
        BiomeParticles.init();
        ParticleManager.init();
    }

    public static void clientSetup() {
        ModPackets.init();
        TickerManager.init();
    }

    public static void levelTick(Minecraft minecraft, Level level) {
        Player player = minecraft.player;
        if (level == null || player == null) {
            TickerManager.TICKERS.clear();
            return;
        }

        if (minecraft.isPaused()) {
            return;
        }

        BiomeParticles.tickBiomeParticles(minecraft, level, player);
        TickerManager.tickTickers();
    }

    public static void configReloaded(ModConfig config) {
        if (config.getModId().equals(MOD_ID)) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null && minecraft.options.getCameraType().isFirstPerson()) {
                if (!ModConfigs.INSTANCE.mobSkullShaders.get()) {
                    ((ShaderManager) minecraft.gameRenderer).ambientSleep$clearShader();
                    return;
                }

                Player player = minecraft.player;
                if (player != null) {
                    Util.applyHelmetShader(player.getInventory().getArmor(3));
                }
            }
        }
    }

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}