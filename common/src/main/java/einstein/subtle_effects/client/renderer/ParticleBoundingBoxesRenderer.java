package einstein.subtle_effects.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.SubtleEffectsClient;
import einstein.subtle_effects.mixin.client.particle.ParticleEngineAccessor;
import einstein.subtle_effects.util.FrustumGetter;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ParticleBoundingBoxesRenderer {

    public static void render(PoseStack poseStack, Camera camera) {
        if (SubtleEffectsClient.DISPLAY_PARTICLE_BOUNDING_BOXES) {
            poseStack.pushPose();
            Minecraft minecraft = Minecraft.getInstance();
            MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
            Vec3 cameraPos = camera.getPosition();
            float partialTicks = Util.getPartialTicks();

            ((ParticleEngineAccessor) minecraft.particleEngine).getParticles().forEach((renderType, particles) -> {
                if (particles == null || particles.isEmpty()) {
                    return;
                }

                Vector3f renderTypeColor = Vec3.fromRGB24(FastColor.ARGB32.color(255, renderType.toString().hashCode())).toVector3f();
                particles.forEach(particle -> {
                    poseStack.pushPose();

                    // Collision/Culling Box
                    poseStack.pushPose();
                    AABB collisionAABB = particle.getBoundingBox();
                    double bbX = (collisionAABB.minX + collisionAABB.maxX) / 2;
                    double bbY = (collisionAABB.minY + collisionAABB.maxY) / 2;
                    double bbZ = (collisionAABB.minZ + collisionAABB.maxZ) / 2;
                    poseStack.translate(bbX - cameraPos.x(), bbY - cameraPos.y(), bbZ - cameraPos.z());

                    collisionAABB = collisionAABB.move(-bbX, -bbY, -bbZ);
                    LevelRenderer.renderLineBox(poseStack, consumer, collisionAABB, 1, 1, 1, 1);

                    // Render Type Box
                    double yHeight = collisionAABB.maxY * 0.2F;
                    AABB renderTypeAABB = new AABB(collisionAABB.minX, collisionAABB.maxY - yHeight, collisionAABB.minZ, collisionAABB.maxX, collisionAABB.maxY + yHeight, collisionAABB.maxZ);
                    LevelRenderer.renderLineBox(poseStack, consumer, renderTypeAABB, renderTypeColor.x(), renderTypeColor.y(), renderTypeColor.z(), 1);

                    poseStack.popPose();

                    // Actual Position Box
                    ParticleAccessor accessor = (ParticleAccessor) particle;
                    double x = Mth.lerp(partialTicks, accessor.getOldX(), accessor.getX()) - cameraPos.x();
                    double y = Mth.lerp(partialTicks, accessor.getOldY(), accessor.getY()) - cameraPos.y();
                    double z = Mth.lerp(partialTicks, accessor.getOldZ(), accessor.getZ()) - cameraPos.z();
                    AABB posAABB = new AABB(-0.05, -0.05, -0.05, 0.05, 0.05, 0.05);
                    posAABB = posAABB.intersect(collisionAABB);

                    poseStack.pushPose();
                    poseStack.translate(x, y, z);
                    LevelRenderer.renderLineBox(poseStack, consumer, posAABB, 1, 0, 0, 1);
                    poseStack.popPose();

                    poseStack.popPose();
                });
            });

            bufferSource.endBatch(RenderType.lines());
            poseStack.popPose();
        }
    }
}
