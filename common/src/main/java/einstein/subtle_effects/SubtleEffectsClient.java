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
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.init.*;
import einstein.subtle_effects.ticking.GeyserManager;
import einstein.subtle_effects.ticking.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.ticking.tickers.ChestBlockEntityTicker;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.ticking.tickers.WaterfallTicker;
import einstein.subtle_effects.ticking.tickers.entity.EntityTickerManager;
import einstein.subtle_effects.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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
    private static boolean DISPLAY_PARTICLE_COUNT = false;
    private static boolean HAS_DISPLAYED_BIRTHDAY_NOTIFICATION = false;
    public static boolean DISPLAY_PARTICLE_BOUNDING_BOXES = false;
    private static Level LEVEL;

    public static void clientSetup() {
        ModConfigs.init();
        ModPipelines.init();
        ModRenderTypes.init();
        ModParticleLayers.init();
        ModPayloads.initClientHandlers();
        ModEntityTickers.init();
        ModBlockTickers.init();
        BiomeParticleManager.init();
        ModDamageListeners.init();
        ModParticleGroups.init();
        ModParticles.init();
        ModSpriteSets.init();
        ModAnimalFedEffectSettings.init();
        CompatHelper.init();
    }

    public static void clientTick(Minecraft minecraft, Level level) {
        Player player = minecraft.player;
        if (level == null || player == null || LEVEL != level) {
            LEVEL = level;

            if (!HAS_CLEARED) {
                clear(level);
                BiomeParticleManager.clear();
                HAS_CLEARED = true;
            }
            return;
        }

        if (minecraft.isPaused() || level.tickRateManager().isFrozen()) {
            return;
        }

        if (!HAS_DISPLAYED_BIRTHDAY_NOTIFICATION && ModConfigs.GENERAL.enableEasterEggs && PartyHatLayer.isModBirthday(true)) {
            // Day before the actual birthday so it doesn't return a negative number when the party hat is enabled the day before
            long years = ChronoUnit.YEARS.between(LocalDate.of(2024, Month.OCTOBER, 3), LocalDate.now());
            sendSystemMsg(minecraft, Component.empty()
                    .append(Component.translatable("chat.subtle_effects.prefix").withStyle(style -> style.withColor(ChatFormatting.BLUE)))
                    .append(CommonComponents.SPACE)
                    .append(Component.translatable("chat.subtle_effects.anniversary.message", Util.getOrdinal(years)))
            );
            HAS_DISPLAYED_BIRTHDAY_NOTIFICATION = true;
        }

        if (DISPLAY_PARTICLE_COUNT) {
            player.displayClientMessage(Component.translatable("ui.subtle_effects.hud.particle_count", minecraft.particleEngine.countParticles()), true);
        }

        ProfilerFiller profiler = Profiler.get();
        profiler.push("subtle_effects");

        profiler.push("biome_particles");
        BiomeParticleManager.tickBiomeParticles(level, player);
        profiler.pop();

        profiler.push("tickers");
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

    public static <T extends Avatar & ClientAvatarEntity> List<RenderLayer<AvatarRenderState, PlayerModel>> registerPlayerRenderLayers(AvatarRenderer<T> renderer, EntityRendererProvider.Context context) {
        List<RenderLayer<AvatarRenderState, PlayerModel>> renderLayers = new ArrayList<>();
        renderLayers.add(new EinsteinSolarSystemLayer<>(renderer, context));

        if (PartyHatLayer.isModBirthday(false)) {
            renderLayers.add(new PartyHatLayer<>(renderer, context));
        }
        return renderLayers;
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

        RequiredArgumentBuilder<T, Boolean> particlesBoundingBoxesEnabled = RequiredArgumentBuilder.<T, Boolean>argument("enabled", BoolArgumentType.bool())
                .executes(context -> toggleParticleBoundingBoxes(minecraft, BoolArgumentType.getBool(context, "enabled")));

        LiteralArgumentBuilder<T> particlesBoundingBoxes = LiteralArgumentBuilder.<T>literal("display_bounding_boxes")
                .executes(context -> toggleParticleBoundingBoxes(minecraft, true))
                .then(particlesBoundingBoxesEnabled);

        LiteralArgumentBuilder<T> particles = LiteralArgumentBuilder.<T>literal("particles")
                .then(particlesClear)
                .then(particlesCount)
                .then(particlesBoundingBoxes);

        // Ticker Args
        LiteralArgumentBuilder<T> tickersClear = LiteralArgumentBuilder.<T>literal("clear")
                .executes(context -> {
                    clear(minecraft.level);
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

    private static int toggleParticleBoundingBoxes(Minecraft minecraft, boolean enabled) {
        DISPLAY_PARTICLE_BOUNDING_BOXES = enabled;

        String enabledString = enabled ? "enable" : "disable";
        sendSystemMsg(minecraft, getMsgTranslation("subtle_effects.particles.display_bounding_boxes." + enabledString + ".success"));
        return 1;
    }

    private static MutableComponent getMsgTranslation(String string) {
        return Component.translatable("commands.subtle_effects." + string);
    }

    private static void sendSystemMsg(Minecraft minecraft, Component component) {
        minecraft.gui.getChat().addMessage(component);
    }

    public static void clear(@Nullable Level level) {
        TickerManager.clear();
        EntityTickerManager.clear(level);
        GeyserManager.ACTIVE_GEYSERS.clear();
        GeyserManager.INACTIVE_GEYSERS.clear();
        WaterfallTicker.WATERFALLS.clear();
        ChestBlockEntityTicker.clear();
    }
}
