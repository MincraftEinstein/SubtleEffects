package einstein.subtle_effects;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import einstein.subtle_effects.compat.CompatHelper;
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
        ModAnimalFedEffectSettings.init();
        CompatHelper.init();
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

        // Particle Args
        LiteralArgumentBuilder<T> particlesClear = LiteralArgumentBuilder.<T>literal("clear")
                .executes(context -> {
                    minecraft.particleEngine.clearParticles();
                    sendSystemMsg(minecraft, getMsgTranslation("subtle_effects.particles.clear.success"));
                    return 1;
                });

        RequiredArgumentBuilder<T, Boolean> particlesCountEnabled = RequiredArgumentBuilder.<T, Boolean>argument("enabled", BoolArgumentType.bool())
                .executes(context -> toggleParticleCount(minecraft, BoolArgumentType.getBool(context, "enabled")));

        LiteralArgumentBuilder<T> particlesCount = LiteralArgumentBuilder.<T>literal("count")
                .executes(context -> toggleParticleCount(minecraft, true))
                .then(particlesCountEnabled);

        LiteralArgumentBuilder<T> particles = LiteralArgumentBuilder.<T>literal("particles")
                .then(particlesClear)
                .then(particlesCount);

        // Ticker Args
        LiteralArgumentBuilder<T> tickersClear = LiteralArgumentBuilder.<T>literal("clear")
                .executes(context -> {
                    TickerManager.clear(minecraft.level);
                    sendSystemMsg(minecraft, getMsgTranslation("subtle_effects.tickers.clear.success"));
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

    private static int toggleParticleCount(Minecraft minecraft, boolean enabled) {
        DISPLAY_PARTICLE_COUNT = enabled;

        String enabledString = enabled ? "enable" : "disable";
        sendSystemMsg(minecraft, getMsgTranslation("subtle_effects.particles.count." + enabledString + ".success"));
        return 1;
    }

    private static MutableComponent getMsgTranslation(String string) {
        return Component.translatable("commands.subtle_effects." + string);
    }

    private static void sendSystemMsg(Minecraft minecraft, Component component) {
        minecraft.gui.getChat().addMessage(component);
    }
}
