package einstein.subtle_effects;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import einstein.subtle_effects.init.*;
import einstein.subtle_effects.ticking.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SubtleEffectsClient {

    private static boolean HAS_CLEARED = false;
    private static boolean DISPLAY_PARTICLE_COUNT = false;
    private static Level LEVEL;

    public static void clientSetup() {
        ModConfigs.init();
        ModParticleRenderTypes.init();
        ModPayloads.initClientHandlers();
        ModEntityTickers.init();
        ModBlockTickers.init();
        BiomeParticleManager.init();
        ModDamageListeners.init();
        ModParticles.init();
        ModSpriteSets.init();
        ModAnimalFedEffectSettings.init();
    }

    public static void clientTick(Minecraft minecraft, Level level) {
        Player player = minecraft.player;
        if (level == null || player == null || LEVEL != level) {
            LEVEL = level;

            if (!HAS_CLEARED) {
                TickerManager.clear(level);
                BiomeParticleManager.clear();
                HAS_CLEARED = true;
            }
            return;
        }

        if (minecraft.isPaused() || level.tickRateManager().isFrozen()) {
            return;
        }

        if (DISPLAY_PARTICLE_COUNT) {
            player.displayClientMessage(Component.translatable("ui.subtle_effects.hud.particle_count", minecraft.particleEngine.countParticles()), true);
        }

        BiomeParticleManager.tickBiomeParticles(level, player);
        TickerManager.tick();
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

        RequiredArgumentBuilder<T, Boolean> particlesCountEnabled = RequiredArgumentBuilder.<T, Boolean>argument("enabled", BoolArgumentType.bool())
                .executes(context -> toggleParticleCount(player, BoolArgumentType.getBool(context, "enabled")));

        LiteralArgumentBuilder<T> particlesCount = LiteralArgumentBuilder.<T>literal("count")
                .executes(context -> toggleParticleCount(player, true))
                .then(particlesCountEnabled);

        LiteralArgumentBuilder<T> particles = LiteralArgumentBuilder.<T>literal("particles")
                .then(particlesClear)
                .then(particlesCount);

        // Ticker Args
        LiteralArgumentBuilder<T> tickersClear = LiteralArgumentBuilder.<T>literal("clear")
                .executes(context -> {
                    TickerManager.clear(player.level());
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

    private static int toggleParticleCount(Player player, boolean enabled) {
        DISPLAY_PARTICLE_COUNT = enabled;

        String enabledString = enabled ? "enable" : "disable";
        sendSystemMsg(player, getMsgTranslation("subtle_effects.particles.count." + enabledString + ".success"));
        return 1;
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
