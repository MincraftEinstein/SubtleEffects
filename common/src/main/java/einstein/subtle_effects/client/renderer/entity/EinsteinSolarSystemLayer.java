package einstein.subtle_effects.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import einstein.subtle_effects.client.model.entity.EinsteinSolarSystemModel;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.EntityRenderStateAccessor;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class EinsteinSolarSystemLayer<T extends PlayerRenderState, V extends PlayerModel> extends RenderLayer<T, V> implements RenderLayerParent<T, EinsteinSolarSystemModel<T>> {

    private static final String UUID = "d71e4b41-9315-499f-a934-ca925421fb38";
    private static final Vector3f[] HEAD_ROTATIONS = {
            new Vector3f(27.6F, 61F, -9.7F),
            new Vector3f(2.5F, 8.6F, -13.8F),
            new Vector3f(-64.7F, 48.2F, -41.9F)
    };
    private final EinsteinSolarSystemModel<T> model;
    private final CustomHeadLayer<T, EinsteinSolarSystemModel<T>> headLayer;
    private final HumanoidArmorLayer<T, EinsteinSolarSystemModel<T>, HumanoidModel<T>> armorLayer;

    @SuppressWarnings("unchecked")
    public EinsteinSolarSystemLayer(RenderLayerParent<?, ?> renderer, EntityRendererProvider.Context context) {
        super((RenderLayerParent<T, V>) renderer);
        model = new EinsteinSolarSystemModel<>(context.bakeLayer(EinsteinSolarSystemModel.MODEL_LAYER));
        headLayer = new CustomHeadLayer<>(this, context.getModelSet());
        armorLayer = new EinsteinSolarSystemArmorLayer<>(this,
                new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                context.getEquipmentRenderer()
        );
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T renderState, float xRot, float yRot) {
        EntityRenderStateAccessor accessor = (EntityRenderStateAccessor) renderState;
        if (accessor.shouldRenderSolarSystem()) {
            int headCount = HEAD_ROTATIONS.length;
            model.hat.visible = renderState.showHat;

            for (int i = 0; i < headCount; i++) {
                float i1 = i + 1;
                float spin = getSpin(accessor, i) * (headCount / i1);
                Vector3f rotation = HEAD_ROTATIONS[i];

                poseStack.pushPose();
                poseStack.mulPose(Axis.YP.rotationDegrees(-180 - renderState.bodyRot));
                poseStack.mulPose(Axis.YP.rotation(spin)); // Spins the head around the player

                poseStack.translate(0.7 * i1, renderState.isCrouching ? 0 : -0.25, 0);
                poseStack.scale(0.8F, 0.8F, 0.8F);

                poseStack.pushPose();
                poseStack.mulPose(Axis.XP.rotationDegrees(rotation.x()));
                poseStack.mulPose(Axis.YP.rotationDegrees(rotation.y()));
                poseStack.mulPose(Axis.ZP.rotationDegrees(rotation.z()));

                poseStack.mulPose(Axis.YP.rotation(spin)); // Spins the head itself

                VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(renderState.skin.texture()));
                int packedOverlay = LivingEntityRenderer.getOverlayCoords(renderState, 0);
                model.setAllVisible(true);
                model.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, -1);

                poseStack.translate(0, 0.25, 0); // Adjusts the renders to align with the lower head model
                headLayer.render(poseStack, bufferSource, packedLight, renderState, xRot, yRot);
                armorLayer.render(poseStack, bufferSource, packedLight, renderState, xRot, yRot);

                poseStack.popPose();
                poseStack.popPose();
            }
        }
    }

    @Override
    public EinsteinSolarSystemModel<T> getModel() {
        return model;
    }

    public static boolean shouldRender(AbstractClientPlayer player) {
        return ModConfigs.GENERAL.enableEasterEggs
                && (player.getStringUUID().equals(UUID) || Services.PLATFORM.isDevelopmentEnvironment())
                && !player.isInvisible();
    }

    public static float getSpin(EntityRenderStateAccessor accessor, float speed) {
        return accessor.getSolarSystemSpin() + (Mth.PI * speed);
    }
}
