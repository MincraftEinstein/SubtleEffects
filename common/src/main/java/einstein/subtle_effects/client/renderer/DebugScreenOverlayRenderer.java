package einstein.subtle_effects.client.renderer;

import com.google.common.collect.EvictingQueue;
import einstein.subtle_effects.SubtleEffectsClient;
import einstein.subtle_effects.mixin.client.particle.ParticleEngineAccessor;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.ticking.tickers.entity.EntityTicker;
import einstein.subtle_effects.ticking.tickers.entity.EntityTickerManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class DebugScreenOverlayRenderer {

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!SubtleEffectsClient.DISPLAY_DEBUG_OVERLAY) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        Map<ParticleRenderType, Queue<Particle>> particles = ((ParticleEngineAccessor) minecraft.particleEngine).getParticles();
        Queue<Particle> particleQueue = particles.get(ParticleRenderType.PARTICLE_SHEET_OPAQUE);
        int maxParticlesPerLayer = (particleQueue instanceof EvictingQueue<Particle> queue ? queue.remainingCapacity() + particleQueue.size() : 16384);
        int layers = particles.size();

        List<Component> leftLines = new ArrayList<>();
        leftLines.add(Component.translatable("ui.subtle_effects.debug_overlay.particle_count", minecraft.particleEngine.countParticles(), layers * maxParticlesPerLayer, layers + " x " + maxParticlesPerLayer));
        leftLines.add(null);
        TickerManager.addDebugInfo(leftLines);

        List<Component> rightLines = new ArrayList<>();
        addEntityInfo(rightLines, minecraft.player, "Player");
        rightLines.add(null);
        addEntityInfo(rightLines, minecraft.crosshairPickEntity, "Targeted Entity");

        renderLines(guiGraphics, leftLines, font, true);
        renderLines(guiGraphics, rightLines, font, false);
    }

    private static void addEntityInfo(List<Component> lines, Entity entity, String title) {
        List<EntityTicker<?>> tickers = EntityTickerManager.getTickersForEntity(entity);
        if (tickers != null && !tickers.isEmpty()) {
            lines.add(Component.literal("[" + title + " Tickers]").withStyle(ChatFormatting.GREEN));
            lines.add(Component.literal("ID: " + BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType())));
            lines.add(Component.literal("Count: " + tickers.size()));

            for (EntityTicker<?> ticker : tickers) {
                lines.add(Component.literal(ticker.getClass().getSimpleName()));
            }
        }
    }

    private static void renderLines(GuiGraphics guiGraphics, List<Component> lines, Font font, boolean left) {
        for (int i = 0; i < lines.size(); i++) {
            Component component = lines.get(i);
            if (component != null) {
                int width = font.width(component);
                int x = left ? 2 : guiGraphics.guiWidth() - 2 - width;
                int y = 2 + font.lineHeight * i;
                guiGraphics.fill(x - 1, y - 1, x + width + 1, y + font.lineHeight - 1, 0x90505050);
                guiGraphics.drawString(font, component, x, y, -1, false);
            }
        }
    }
}
