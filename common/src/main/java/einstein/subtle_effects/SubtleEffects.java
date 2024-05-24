package einstein.subtle_effects;

import einstein.subtle_effects.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.init.*;
import einstein.subtle_effects.tickers.TickerManager;
import einstein.subtle_effects.util.ShaderManager;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubtleEffects {

    public static final String MOD_ID = "subtle_effects";
    public static final String MOD_NAME = "Subtle Effects";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        ModSounds.init();
        ModParticles.init();
        ModPackets.init();
        BiomeParticleManager.init();
        ModDamageListeners.init();
    }

    public static void clientSetup() {
        ModPackets.init();
        ModTickers.init();
        ModBlockTickers.init();
    }

    public static void levelTick(Minecraft minecraft, Level level) {
        Player player = minecraft.player;
        if (level == null || player == null) {
            TickerManager.clear();
            BiomeParticleManager.clear();
            return;
        }

        if (minecraft.isPaused()) {
            return;
        }

        BiomeParticleManager.tickBiomeParticles(level, player);
        TickerManager.tickTickers(level);
    }

    public static void configReloaded(ModConfig config) {
        if (config.getModId().equals(MOD_ID)) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null && minecraft.options.getCameraType().isFirstPerson()) {
                if (!ModConfigs.INSTANCE.mobSkullShaders.get()) {
                    ((ShaderManager) minecraft.gameRenderer).subtleEffects$clearShader();
                    return;
                }

                Player player = minecraft.player;
                if (player != null) {
                    Util.applyHelmetShader(player.getInventory().getArmor(3));
                }
            }
            TickerManager.clear();
            BiomeParticleManager.clear();
        }
    }

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}