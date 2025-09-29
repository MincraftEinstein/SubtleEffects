package einstein.subtle_effects.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import einstein.subtle_effects.platform.Services;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

import java.util.Calendar;

import static einstein.subtle_effects.SubtleEffects.loc;
import static einstein.subtle_effects.init.ModConfigs.GENERAL;

public class AnniversaryHatLayer<T extends AbstractClientPlayer, V extends HumanoidModel<T>> extends RenderLayer<T, V> {

    @SuppressWarnings("unchecked")
    public AnniversaryHatLayer(RenderLayerParent<?, ?> renderer, EntityRendererProvider.Context context) {
        super((RenderLayerParent<T, V>) renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (shouldRender(player)) {
            poseStack.pushPose();
            getParentModel().getHead().translateAndRotate(poseStack);

            poseStack.translate(-2f * PIXEL, -8f * PIXEL, -2f * PIXEL);
            var texture = loc("textures/misc/party_hat.png");

            // Vertex data: Position, Color, UV0 (texture), UV1 (overlay), UV2 (light), Normal
            var buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));

            var color = FastColor.ARGB32.color(255, 255, 255);
            var pose = poseStack.last();
            drawHalf(buffer, poseStack, pose, color, packedLight);

            poseStack.rotateAround(Axis.YN.rotationDegrees(-270f), 2f * PIXEL, 0f, 2f * PIXEL);
            drawHalf(buffer, poseStack, pose, color, packedLight);

            poseStack.popPose();
        }
    }

    public static float PIXEL = 1f / 16f;

    static void drawHalf(VertexConsumer buffer, PoseStack poseStack, PoseStack.Pose pose, int color, int packedLight) {
        addVertex(buffer, pose, 2f, -6f, 2f, color, 2.5f, 0f, packedLight);
        addVertex(buffer, pose, 4f, 0f, 0f, color, 0f, 6f, packedLight);
        addVertex(buffer, pose, 4f, 0f, 4f, color, 5f, 6f, packedLight);
        addVertex(buffer, pose, 2f, -6f, 2f, color, 2.5f, 0f, packedLight);

        poseStack.rotateAround(Axis.YN.rotationDegrees(90f), 2f * PIXEL, 0f, 2f * PIXEL);
        addVertex(buffer, pose, 2f, -6f, 2f, color, 7.5f, 0f, packedLight);
        addVertex(buffer, pose, 4f, 0f, 0f, color, 5f, 6f, packedLight);
        addVertex(buffer, pose, 4f, 0f, 4f, color, 10f, 6f, packedLight);
        addVertex(buffer, pose, 2f, -6f, 2f, color, 7.5f, 0f, packedLight);
    }

    static void addVertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, int color, float u, float v, int light) {
        consumer.addVertex(pose, x * PIXEL, y * PIXEL, z * PIXEL)
                .setColor(color)
                .setUv(u * PIXEL, v * PIXEL)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, -1.0F, 0.0F);
    }

    public static boolean shouldRender(AbstractClientPlayer player) {
        return (GENERAL.enableEasterEggs) && !player.isInvisible();
    }

    @Override
    protected ResourceLocation getTextureLocation(T entity) {
        return super.getTextureLocation(entity);
    }

    public static boolean isModAnniversary() {
        if (Services.PLATFORM.isDevelopmentEnvironment()) {
            return true;
        }

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        return month == Calendar.OCTOBER && date >= 3 && date <= 5;
    }
}
