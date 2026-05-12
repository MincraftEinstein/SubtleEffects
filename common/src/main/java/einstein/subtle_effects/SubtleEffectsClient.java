package einstein.subtle_effects;

import com.google.common.collect.EvictingQueue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import einstein.subtle_effects.mixin.client.particle.ParticleEngineAccessor;
import einstein.subtle_effects.ticking.GeyserManager;
import einstein.subtle_effects.ticking.biome_particles.BiomeParticleManager;
import einstein.subtle_effects.ticking.tickers.ChestBlockEntityTicker;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.ticking.tickers.WaterfallTicker;
import einstein.subtle_effects.ticking.tickers.entity.EntityTickerManager;
import einstein.subtle_effects.util.FrustumGetter;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.*;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Supplier;

public class SubtleEffectsClient {

    private static boolean HAS_CLEARED = false;
    private static boolean DISPLAY_DEBUG_OVERLAY = false;
    private static boolean HAS_DISPLAYED_BIRTHDAY_NOTIFICATION = false;
    private static boolean DISPLAY_PARTICLE_BOUNDING_BOXES = false;
    private static Level LEVEL;

    public static void clientSetup() {
        ModConfigs.init();
        ModRenderTypes.init();
        ModParticleRenderTypes.init();
        ModPayloads.initClientHandlers();
        ModEntityTickers.init();
        ModBlockTickers.init();
        BiomeParticleManager.init();
        ModDamageListeners.init();
        ModParticles.init();
        ModSpriteSets.init();
        ModAnimalFedEffectSettings.init();
        ColorProviderType.init();
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
                    clear(minecraft.level);
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

    public static void clear(@Nullable Level level) {
        TickerManager.clear();
        EntityTickerManager.clear(level);
        GeyserManager.ACTIVE_GEYSERS.clear();
        GeyserManager.INACTIVE_GEYSERS.clear();
        WaterfallTicker.WATERFALLS.clear();
        ChestBlockEntityTicker.clear();
    }

    public static void renderParticleBoundingBoxes(PoseStack poseStack, Camera camera) {
        if (DISPLAY_PARTICLE_BOUNDING_BOXES) {
            poseStack.pushPose();
            Minecraft minecraft = Minecraft.getInstance();
            MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
            Vec3 cameraPos = camera.getPosition();
            Frustum frustum = ((FrustumGetter) minecraft.levelRenderer).subtleEffects$getCullingFrustum();
            float partialTicks = Util.getPartialTicks();

            ((ParticleEngineAccessor) minecraft.particleEngine).getParticles().forEach((renderType, particles) -> {
                if (particles == null || particles.isEmpty()) {
                    return;
                }

                Vector3f renderTypeColor = Vec3.fromRGB24(FastColor.ARGB32.color(255, renderType.toString().hashCode())).toVector3f();
                particles.forEach(particle -> {
                    AABB aabb = particle.getBoundingBox();
                    if (frustum.isVisible(aabb)) {
                        poseStack.pushPose();
                        ParticleAccessor accessor = (ParticleAccessor) particle;
                        double x = Mth.lerp(partialTicks, accessor.getOldX(), accessor.getX()) - cameraPos.x();
                        double y = Mth.lerp(partialTicks, accessor.getOldY(), accessor.getY()) - cameraPos.y();
                        double z = Mth.lerp(partialTicks, accessor.getOldZ(), accessor.getZ()) - cameraPos.z();
                        poseStack.translate(x, y, z);

                        aabb = aabb.move(-accessor.getX(), -accessor.getY(), -accessor.getZ());
                        LevelRenderer.renderLineBox(poseStack, consumer, aabb, 1, 1, 1, 1);

                        AABB renderTypeAABB = new AABB(aabb.minX, aabb.maxY - 0.02, aabb.minZ, aabb.maxX, aabb.maxY + 0.02, aabb.maxZ);
                        LevelRenderer.renderLineBox(poseStack, consumer, renderTypeAABB, renderTypeColor.x(), renderTypeColor.y(), renderTypeColor.z(), 1);

                        poseStack.popPose();
                    }
                });
            });

            bufferSource.endBatch(RenderType.lines());
            poseStack.popPose();
        }
    }

    public static void renderDebugScreenOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!DISPLAY_DEBUG_OVERLAY) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        List<Component> lines = new ArrayList<>();
        Map<ParticleRenderType, Queue<Particle>> particles = ((ParticleEngineAccessor) minecraft.particleEngine).getParticles();
        Queue<Particle> particleQueue = particles.get(ParticleRenderType.PARTICLE_SHEET_OPAQUE);
        int maxParticlesPerLayer = (particleQueue instanceof EvictingQueue<Particle> queue ? queue.remainingCapacity() + particleQueue.size() : 16384);
        int layers = particles.size();

        lines.add(Component.translatable("ui.subtle_effects.debug_overlay.particle_count", minecraft.particleEngine.countParticles(), layers * maxParticlesPerLayer, layers + " x " + maxParticlesPerLayer));
        lines.add(null);
        TickerManager.addDebugInfo(lines);

        for (int i = 0; i < lines.size(); i++) {
            Component component = lines.get(i);
            if (component != null) {
                Font font = minecraft.font;
                int width = font.width(component);
                int x = 2;
                int y = 2 + font.lineHeight * i;
                guiGraphics.fill(x - 1, y - 1, x + width + 1, y + font.lineHeight - 1, 0x90505050);
                guiGraphics.drawString(font, component, x, y, -1, false);
            }
        }
    }
}
