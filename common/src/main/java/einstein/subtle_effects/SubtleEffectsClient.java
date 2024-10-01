package einstein.subtle_effects;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import einstein.subtle_effects.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.init.*;
import einstein.subtle_effects.tickers.TickerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
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

    public static <T extends SharedSuggestionProvider> void registerClientCommands(CommandDispatcher<T> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(LiteralArgumentBuilder.<T>literal("subtle_effects")
                .then(LiteralArgumentBuilder.<T>literal("particles")
                        .then(LiteralArgumentBuilder.<T>literal("clear")
                                .executes(context -> {
                                    Minecraft.getInstance().particleEngine.clearParticles();
                                    return 1;
                                })
                        )
                )
                .then(LiteralArgumentBuilder.<T>literal("tickers")
                        .then(LiteralArgumentBuilder.<T>literal("clear")
                                .executes(context -> {
                                    TickerManager.clear();
                                    return 1;
                                })
                        )
                )
        );
    }
}
