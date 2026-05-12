package einstein.subtle_effects.client.renderer;

import com.google.common.collect.EvictingQueue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.SubtleEffectsClient;
import einstein.subtle_effects.mixin.client.particle.ParticleEngineAccessor;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.ticking.tickers.entity.EntityTicker;
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
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class DebugRenderers {

    public static void renderParticleBoundingBoxes(PoseStack poseStack, Camera camera) {
        if (SubtleEffectsClient.DISPLAY_PARTICLE_BOUNDING_BOXES) {
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
