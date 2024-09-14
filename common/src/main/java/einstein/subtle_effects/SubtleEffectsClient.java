package einstein.subtle_effects;

import einstein.subtle_effects.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.init.*;
import einstein.subtle_effects.tickers.TickerManager;
import einstein.subtle_effects.util.ShaderManager;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SubtleEffectsClient {

    private static boolean HAS_CLEARED = false;

    public static void clientSetup() {
        ModPackets.initClientHandlers();
        ModTickers.init();
        ModBlockTickers.init();
        BiomeParticleManager.init();
        ModDamageListeners.init();
        ModParticles.init();
    }

    public static void clientTick(Minecraft minecraft, Level level) {
        Player player = minecraft.player;
        if (level == null || player == null) {
            if (!HAS_CLEARED) {
                TickerManager.clear();
                BiomeParticleManager.clear();
                HAS_CLEARED = true;
            }
            return;
        }

        if (minecraft.isPaused()) {
            return;
        }

        BiomeParticleManager.tickBiomeParticles(level, player);
        TickerManager.tickTickers(level);
        HAS_CLEARED = false;
    }

    public static void configReloaded() {
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