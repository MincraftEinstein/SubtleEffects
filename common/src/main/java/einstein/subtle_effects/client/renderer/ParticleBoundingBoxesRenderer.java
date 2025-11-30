package einstein.subtle_effects.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.SubtleEffectsClient;
import einstein.subtle_effects.init.ModRenderStateAttachmentKeys;
import einstein.subtle_effects.mixin.client.particle.ParticleEngineAccessor;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.SingleQuadParticleAccessor;
import einstein.subtle_effects.util.RenderStateAttachmentAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticleBoundingBoxesRenderer {

    private static final Map<SingleQuadParticle.Layer, Vector3f> LAYER_TO_COLOR = net.minecraft.util.Util.make(new HashMap<>(), map -> {
        map.put(SingleQuadParticle.Layer.TERRAIN, stringToColor("TERRAIN"));
        map.put(SingleQuadParticle.Layer.OPAQUE, stringToColor("OPAQUE"));
        map.put(SingleQuadParticle.Layer.TRANSLUCENT, stringToColor("TRANSLUCENT"));
    });

    private static Vector3f stringToColor(String color) {
        // TODO make sure this is correct (might need to add alpha)
        return ARGB.vector3fFromRGB24( color.hashCode());
    }

    public static void extractParticleBoundingBoxes(LevelRenderState levelRenderState, Camera camera, Frustum frustum) {
        if (SubtleEffectsClient.DISPLAY_PARTICLE_BOUNDING_BOXES) {
            Vec3 cameraPos = camera.position();
            Minecraft minecraft = Minecraft.getInstance();
            List<ParticleBoundingBoxRenderState> renderStates = new ArrayList<>();
            float partialTicks = Util.getPartialTicks();
            Frustum particleFrustum = frustum.offset(-3); // offset to match the frustum used by the particle engine

            ((ParticleEngineAccessor) minecraft.particleEngine).getParticles().forEach((renderType, particleGroup) -> {
                if (particleGroup == null || particleGroup.isEmpty()) {
                    return;
                }

                Vector3f renderTypeColor = stringToColor(renderType.name());
                particleGroup.getAll().forEach(particle -> {
                    AABB aabb = particle.getBoundingBox();
                    if (particleFrustum.isVisible(aabb)) {
                        ParticleAccessor accessor = (ParticleAccessor) particle;
                        double x = Mth.lerp(partialTicks, accessor.getOldX(), accessor.getX()) - cameraPos.x();
                        double y = Mth.lerp(partialTicks, accessor.getOldY(), accessor.getY()) - cameraPos.y();
                        double z = Mth.lerp(partialTicks, accessor.getOldZ(), accessor.getZ()) - cameraPos.z();
                        Vector3f color = renderTypeColor;
                        if (particle instanceof SingleQuadParticleAccessor quadAccessor) {
                            Vector3f layerColor = LAYER_TO_COLOR.get(quadAccessor.subtleEffects$getLayer());

                            if (layerColor != null) {
                                color = layerColor;
                            }
                        }

                        aabb = aabb.move(-accessor.getX(), -accessor.getY(), -accessor.getZ());
                        AABB renderTypeAABB = new AABB(aabb.minX, aabb.maxY - 0.02, aabb.minZ, aabb.maxX, aabb.maxY + 0.02, aabb.maxZ);
                        renderStates.add(new ParticleBoundingBoxRenderState(x, y, z, color, aabb, renderTypeAABB));
                    }
                });
            });

            if (!renderStates.isEmpty()) {
                ((RenderStateAttachmentAccessor) levelRenderState).subtleEffects$set(ModRenderStateAttachmentKeys.PARTICLE_BOUNDING_BOXES, renderStates);
            }
        }
    }

    public static void renderParticleBoundingBoxes(PoseStack poseStack, LevelRenderState levelRenderState) {
        if (SubtleEffectsClient.DISPLAY_PARTICLE_BOUNDING_BOXES) {
            List<ParticleBoundingBoxRenderState> renderStates = ((RenderStateAttachmentAccessor) levelRenderState).subtleEffects$get(ModRenderStateAttachmentKeys.PARTICLE_BOUNDING_BOXES);
            if (renderStates != null) {
                VertexConsumer consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderTypes.lines());

                renderStates.forEach(renderState -> {
                    poseStack.pushPose();
                    poseStack.translate(renderState.x(), renderState.y(), renderState.z());

                    PoseStack.Pose lastPose = poseStack.last();
                    // TODO kill me
//                    ShapeRenderer.renderLineBox(lastPose, consumer, renderState.aabb(), 1, 1, 1, 1);

                    Vector3f color = renderState.color();
                    if (color != null) {
//                        ShapeRenderer.renderLineBox(lastPose, consumer, renderState.renderTypeAABB(), renderState.color.x(), renderState.color.y(), renderState.color.z(), 1);
                    }

                    poseStack.popPose();
                });
            }
        }
    }

    public record ParticleBoundingBoxRenderState(double x, double y, double z, Vector3f color, AABB aabb,
                                                 AABB renderTypeAABB) {

    }
}
