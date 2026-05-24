package einstein.subtle_effects;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import einstein.subtle_effects.client.model.entity.EinsteinSolarSystemModel;
import einstein.subtle_effects.client.model.entity.PartyHatModel;
import einstein.subtle_effects.client.model.particle.SplashParticleModel;
import einstein.subtle_effects.client.renderer.entity.EinsteinSolarSystemLayer;
import einstein.subtle_effects.client.renderer.entity.PartyHatLayer;
import einstein.subtle_effects.data.*;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import einstein.subtle_effects.init.*;
import einstein.subtle_effects.ticking.BiomeEffectsManager;
import einstein.subtle_effects.ticking.GeyserManager;
import einstein.subtle_effects.ticking.tickers.ChestBlockEntityTicker;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.ticking.tickers.WaterfallTicker;
import einstein.subtle_effects.ticking.tickers.entity.EntityTickerManager;
import einstein.subtle_effects.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.*;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SubtleEffectsClient {

    private static boolean HAS_CLEARED = false;
    public static boolean DISPLAY_DEBUG_OVERLAY = false;
    private static boolean HAS_DISPLAYED_BIRTHDAY_NOTIFICATION = false;
    public static boolean DISPLAY_PARTICLE_BOUNDING_BOXES = false;
    private static Level LEVEL;

    public static void clientSetup() {
        ModConfigs.init();
        ModRenderTypes.init();
        ModParticleRenderTypes.init();
        ModEntityTickers.init();
        ModBlockTickers.init();
        BiomeEffectsManager.init();
        ModDamageListeners.init();
        ModParticleProviders.init();
        ModSpriteSets.init();
        ModAnimalFedEffectSettings.init();
        ColorProviderType.init();
    }

    public static void clientTick(Minecraft minecraft, Level level) {
        Player player = minecraft.player;
        if (level == null || player == null || LEVEL != level) {
            LEVEL = level;

            if (!HAS_CLEARED) {
                clear();
                HAS_CLEARED = true;
            }
            return;
        }

        if (minecraft.isPaused() || !level.tickRateManager().runsNormally()) {
            return;
        }

        if (!HAS_DISPLAYED_BIRTHDAY_NOTIFICATION && ModConfigs.GENERAL.enableEasterEggs && PartyHatLayer.isModBirthday(true)) {
            // Day before the actual birthday so it doesn't return a negative number when the party hat is enabled the day before
            long years = ChronoUnit.YEARS.between(LocalDate.of(2024, Month.OCTOBER, 3), LocalDate.now());
            sendSystemMsg(player, Component.empty()
                    .append(Component.translatable("chat.subtle_effects.prefix").withStyle(style -> style.withColor(ChatFormatting.BLUE)))
                    .append(CommonComponents.SPACE)
                    .append(Component.translatable("chat.subtle_effects.anniversary.message", Util.formatOrdinal(years), Component.translatable("chat.subtle_effects.anniversary.message.click_event")
                            .withStyle(style -> style.withUnderlined(true)
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.subtle_effects.anniversary.message.click_event.tooltip")))
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/configure subtle_effects general")))
                    ))
            );
            HAS_DISPLAYED_BIRTHDAY_NOTIFICATION = true;
        }

        ProfilerFiller profiler = minecraft.getProfiler();
        profiler.push("subtle_effects");

        profiler.push("tickers");
        EntityTickerManager.tick();
        TickerManager.tick();
        profiler.pop();

        HAS_CLEARED = false;
        profiler.pop();
    }

    public static Map<ModelLayerLocation, Supplier<LayerDefinition>> registerModelLayers() {
        Map<ModelLayerLocation, Supplier<LayerDefinition>> layers = new HashMap<>();
        layers.put(EinsteinSolarSystemModel.MODEL_LAYER, EinsteinSolarSystemModel::createLayer);
        layers.put(PartyHatModel.MODEL_LAYER, PartyHatModel::createLayer);
        layers.put(SplashParticleModel.MODEL_LAYER, SplashParticleModel::createLayer);
        return layers;
    }

    public static List<RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>> registerPlayerRenderLayers(PlayerRenderer renderer, EntityRendererProvider.Context context) {
        List<RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>> renderLayers = new ArrayList<>();
        renderLayers.add(new EinsteinSolarSystemLayer<>(renderer, context));

        if (PartyHatLayer.isModBirthday(false)) {
            renderLayers.add(new PartyHatLayer<>(renderer, context));
        }
        return renderLayers;
    }

    public static <T extends PreparableReloadListener & NamedReloadListener> List<T> registerReloadListeners() {
        List<T> reloadListeners = new ArrayList<>();
        registerReloadListener(reloadListeners, new SparkProviderReloadListener());
        registerReloadListener(reloadListeners, new MobSkullShaderReloadListener());
        registerReloadListener(reloadListeners, new BCWPPackManager());
        registerReloadListener(reloadListeners, new FluidDefinitionReloadListener());
        registerReloadListener(reloadListeners, new BurningEffectsReloadListener());
        return reloadListeners;
    }

    @SuppressWarnings("unchecked")
    public static <T extends PreparableReloadListener & NamedReloadListener, V extends PreparableReloadListener & NamedReloadListener> void registerReloadListener(List<T> reloadListeners, V listener) {
        reloadListeners.add((T) listener);
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

        RequiredArgumentBuilder<T, Boolean> particlesBoundingBoxesEnabled = RequiredArgumentBuilder.<T, Boolean>argument("enabled", BoolArgumentType.bool())
                .executes(context -> toggleParticleBoundingBoxes(player, BoolArgumentType.getBool(context, "enabled")));

        LiteralArgumentBuilder<T> particlesBoundingBoxes = LiteralArgumentBuilder.<T>literal("display_bounding_boxes")
                .executes(context -> toggleParticleBoundingBoxes(player, true))
                .then(particlesBoundingBoxesEnabled);

        LiteralArgumentBuilder<T> particles = LiteralArgumentBuilder.<T>literal("particles")
                .then(particlesClear)
                .then(particlesBoundingBoxes);

        // Ticker Args
        LiteralArgumentBuilder<T> tickersClear = LiteralArgumentBuilder.<T>literal("clear")
                .executes(context -> {
                    clear();
                    sendSystemMsg(player, getMsgTranslation("subtle_effects.tickers.clear.success"));
                    return 1;
                });
        LiteralArgumentBuilder<T> tickers = LiteralArgumentBuilder.<T>literal("tickers")
                .then(tickersClear);

        // Debug Args
        RequiredArgumentBuilder<T, Boolean> debugOverlayEnabled = RequiredArgumentBuilder.<T, Boolean>argument("enabled", BoolArgumentType.bool())
                .executes(context -> toggleDebugOverlay(player, BoolArgumentType.getBool(context, "enabled")));

        LiteralArgumentBuilder<T> debugOverlay = LiteralArgumentBuilder.<T>literal("debug_screen")
                .executes(context -> toggleDebugOverlay(player, !DISPLAY_DEBUG_OVERLAY))
                .then(debugOverlayEnabled);

        // SE Command
        LiteralArgumentBuilder<T> subtleEffects = LiteralArgumentBuilder.<T>literal("subtle_effects")
                .then(particles)
                .then(tickers)
                .then(debugOverlay);

        LiteralCommandNode<T> subtleEffectsNode = dispatcher.register(subtleEffects);
        dispatcher.register(LiteralArgumentBuilder.<T>literal("se").redirect(subtleEffectsNode));
    }

    private static int toggleDebugOverlay(Player player, boolean enabled) {
        DISPLAY_DEBUG_OVERLAY = enabled;

        String enabledString = enabled ? "enable" : "disable";
        sendSystemMsg(player, getMsgTranslation("subtle_effects.particles.count." + enabledString + ".success"));
        return 1;
    }

    private static int toggleParticleBoundingBoxes(Player player, boolean enabled) {
        DISPLAY_PARTICLE_BOUNDING_BOXES = enabled;

        String enabledString = enabled ? "enable" : "disable";
        sendSystemMsg(player, getMsgTranslation("subtle_effects.particles.display_bounding_boxes." + enabledString + ".success"));
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

    public static void clear() {
        TickerManager.clear();
        EntityTickerManager.clear();
        GeyserManager.ACTIVE_GEYSERS.clear();
        GeyserManager.INACTIVE_GEYSERS.clear();
        WaterfallTicker.WATERFALLS.clear();
        ChestBlockEntityTicker.clear();
        BiomeEffectsManager.init();
    }
}
