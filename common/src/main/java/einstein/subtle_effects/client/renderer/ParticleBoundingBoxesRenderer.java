package einstein.subtle_effects.client.renderer;

import einstein.subtle_effects.SubtleEffectsClient;
import einstein.subtle_effects.init.ModParticleLayers;
import einstein.subtle_effects.init.ModRenderStateAttachmentKeys;
import einstein.subtle_effects.mixin.client.particle.ParticleEngineAccessor;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.RenderStateAttachmentAccessor;
import einstein.subtle_effects.util.SingleQuadParticleAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticleBoundingBoxesRenderer {

    private static final Map<SingleQuadParticle.Layer, Integer> LAYER_TO_COLOR = net.minecraft.util.Util.make(new HashMap<>(), map -> {
        map.put(SingleQuadParticle.Layer.OPAQUE_TERRAIN, 0xff_00ff00);
        map.put(SingleQuadParticle.Layer.TRANSLUCENT_TERRAIN, 0xff_00ff00);
        map.put(SingleQuadParticle.Layer.OPAQUE_ITEMS, 0xff_f0ff00);
        map.put(SingleQuadParticle.Layer.TRANSLUCENT_ITEMS, 0xff_0ffff0);
        map.put(SingleQuadParticle.Layer.OPAQUE, 0xff_ffff00);
        map.put(SingleQuadParticle.Layer.TRANSLUCENT, 0xff_00ffff);
        map.put(ModParticleLayers.BLENDED, 0xff_ff00ff);
    });

    public static void extract(LevelRenderState levelRenderState, CameraRenderState camera) {
        if (SubtleEffectsClient.DISPLAY_PARTICLE_BOUNDING_BOXES) {
            Vec3 cameraPos = camera.pos;
            Minecraft minecraft = Minecraft.getInstance();
            List<RenderState> renderStates = new ArrayList<>();
            float partialTicks = Util.getPartialTicks();
            Frustum particleFrustum = camera.cullFrustum.offset(-3); // offset to match the frustum used by the particle engine

            ((ParticleEngineAccessor) minecraft.particleEngine).getParticles().forEach((renderType, group) -> {
                if (group == null || group.isEmpty()) {
                    return;
                }

                int renderTypeColor = renderType.name().hashCode();
                group.particles.forEach(particle -> {
                    AABB collisionAABB = particle.getBoundingBox();
                    if (particleFrustum.isVisible(collisionAABB)) {
                        double bbX = (collisionAABB.minX + collisionAABB.maxX) / 2;
                        double bbY = (collisionAABB.minY + collisionAABB.maxY) / 2;
                        double bbZ = (collisionAABB.minZ + collisionAABB.maxZ) / 2;

                        collisionAABB = collisionAABB.move(-bbX, -bbY, -bbZ);

                        double yHeight = collisionAABB.maxY * 0.2F;
                        AABB renderTypeAABB = new AABB(collisionAABB.minX, collisionAABB.maxY - yHeight, collisionAABB.minZ, collisionAABB.maxX, collisionAABB.maxY + yHeight, collisionAABB.maxZ);
                        ParticleAccessor accessor = (ParticleAccessor) particle;
                        double x = Mth.lerp(partialTicks, accessor.getOldX(), accessor.getX());
                        double y = Mth.lerp(partialTicks, accessor.getOldY(), accessor.getY());
                        double z = Mth.lerp(partialTicks, accessor.getOldZ(), accessor.getZ());

                        AABB posAABB = new AABB(-0.05, -0.05, -0.05, 0.05, 0.05, 0.05);
                        posAABB = posAABB.intersect(collisionAABB);
                        posAABB = posAABB.move(x, y, z);

                        renderStates.add(new RenderState(posAABB,
                                collisionAABB.move(bbX, bbY, bbZ),
                                renderTypeAABB.move(bbX, bbY, bbZ),
                                getLayerColor(particle, renderTypeColor)
                        ));
                    }
                });
            });

            if (!renderStates.isEmpty()) {
                ((RenderStateAttachmentAccessor) levelRenderState).subtleEffects$set(ModRenderStateAttachmentKeys.PARTICLE_BOUNDING_BOXES, renderStates);
            }
        }
    }

    private static int getLayerColor(Particle particle, int renderTypeColor) {
        if (particle instanceof SingleQuadParticleAccessor accessor) {
            Integer i = LAYER_TO_COLOR.get(accessor.subtleEffects$getLayer());
            if (i != null) {
                return i;
            }
        }
        return renderTypeColor;
    }

    public static void render(LevelRenderState levelRenderState) {
        if (SubtleEffectsClient.DISPLAY_PARTICLE_BOUNDING_BOXES) {
            List<RenderState> renderStates = ((RenderStateAttachmentAccessor) levelRenderState).subtleEffects$get(ModRenderStateAttachmentKeys.PARTICLE_BOUNDING_BOXES);
            if (renderStates == null) {
                return;
            }

            renderStates.forEach(renderState -> {
                Gizmos.cuboid(renderState.collisionAABB, GizmoStyle.stroke(0xFFFFFFFF));
                Gizmos.cuboid(renderState.renderTypeAABB, GizmoStyle.stroke(renderState.renderTypeColor));
                Gizmos.cuboid(renderState.posAABB, GizmoStyle.stroke(0xFFFF0000));
            });
        }
    }

    public record RenderState(AABB posAABB, AABB collisionAABB, AABB renderTypeAABB, int renderTypeColor) {

    }
}
