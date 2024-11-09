package einstein.subtle_effects;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import einstein.subtle_effects.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.init.*;
import einstein.subtle_effects.tickers.TickerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
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

        if (minecraft.isPaused()) {
            return;
        }

        BiomeParticleManager.tickBiomeParticles(level, player);
        TickerManager.tickTickers(level);
        HAS_CLEARED = false;
    }

    public static <T extends SharedSuggestionProvider> void registerClientCommands(CommandDispatcher<T> dispatcher, CommandBuildContext buildContext) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        dispatcher.register(LiteralArgumentBuilder.<T>literal("subtle_effects")
                .then(LiteralArgumentBuilder.<T>literal("particles")
                        .then(LiteralArgumentBuilder.<T>literal("clear")
                                .executes(context -> {
                                    minecraft.particleEngine.clearParticles();
                                    sendSystemMsg(player, Component.translatable("commands.subtle_effects.subtle_effects.particles.clear.success"));
                                    return 1;
                                })
                        )
                )
                .then(LiteralArgumentBuilder.<T>literal("tickers")
                        .then(LiteralArgumentBuilder.<T>literal("clear")
                                .executes(context -> {
                                    TickerManager.clear();
                                    sendSystemMsg(player, Component.translatable("commands.subtle_effects.subtle_effects.tickers.clear.success"));
                                    return 1;
                                })
                        )
                )
        );
    }

    private static void sendSystemMsg(Player player, Component component) {
        if (player != null) {
            player.sendSystemMessage(component);
        }
    }
}
