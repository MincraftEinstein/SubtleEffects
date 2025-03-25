package einstein.subtle_effects;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import einstein.subtle_effects.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.init.*;
import einstein.subtle_effects.tickers.TickerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SubtleEffectsClient {

    private static boolean HAS_CLEARED = false;
    private static Level LEVEL;

    public static void clientSetup() {
        ModConfigs.init();
        ModPackets.initClientHandlers();
        ModTickers.init();
        ModBlockTickers.init();
        BiomeParticleManager.init();
        ModDamageListeners.init();
        ModParticles.init();
    }

    public static void clientTick(Minecraft minecraft, Level level) {
        Player player = minecraft.player;
        if (level == null || player == null || LEVEL != level) {
            LEVEL = level;

            if (!HAS_CLEARED) {
                TickerManager.clear();
                BiomeParticleManager.clear();
                HAS_CLEARED = true;
            }
            return;
        }

        if (minecraft.isPaused() || level.tickRateManager().isFrozen()) {
            return;
        }

        BiomeParticleManager.tickBiomeParticles(level, player);
        TickerManager.tickTickers(level);
        HAS_CLEARED = false;
    }

    public static <T extends SharedSuggestionProvider> void registerClientCommands(CommandDispatcher<T> dispatcher, CommandBuildContext buildContext) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        // Particle Args
        LiteralArgumentBuilder<T> particlesClear = LiteralArgumentBuilder.<T>literal("clear")
                .executes(context -> {
                    minecraft.particleEngine.clearParticles();
                    sendSystemMsg(player, getMsgTranslation("subtle_effects.particles.clear.success"));
                    return 1;
                });
        LiteralArgumentBuilder<T> particles = LiteralArgumentBuilder.<T>literal("particles")
                .then(particlesClear);

        // Ticker Args
        LiteralArgumentBuilder<T> tickersClear = LiteralArgumentBuilder.<T>literal("clear")
                .executes(context -> {
                    TickerManager.clear();
                    sendSystemMsg(player, getMsgTranslation("subtle_effects.tickers.clear.success"));
                    return 1;
                });
        LiteralArgumentBuilder<T> tickers = LiteralArgumentBuilder.<T>literal("tickers")
                .then(tickersClear);

        // SE Command
        LiteralArgumentBuilder<T> subtleEffects = LiteralArgumentBuilder.<T>literal("subtle_effects")
                .then(particles)
                .then(tickers);

        LiteralCommandNode<T> subtleEffectsNode = dispatcher.register(subtleEffects);
        dispatcher.register(LiteralArgumentBuilder.<T>literal("se").redirect(subtleEffectsNode));
    }

    private static MutableComponent getMsgTranslation(String string) {
        return Component.translatable("commands.subtle_effects." + string);
    }

    private static void sendSystemMsg(Player player, Component component) {
        if (player != null) {
            player.sendSystemMessage(component);
        }
    }
}
