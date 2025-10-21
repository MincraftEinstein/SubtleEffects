package einstein.subtle_effects.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import einstein.subtle_effects.client.model.entity.EinsteinSolarSystemModel;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.EntityRenderStateAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import static einstein.subtle_effects.init.ModRenderStateKeys.*;

public class EinsteinSolarSystemLayer<T extends PlayerRenderState, V extends HumanoidModel<T>> extends RenderLayer<T, V> implements RenderLayerParent<T, EinsteinSolarSystemModel<T>> {

    private static final Vector3f[] HEAD_ROTATIONS = {
            new Vector3f(27.6F, 61F, -9.7F),
            new Vector3f(2.5F, 8.6F, -13.8F),
            new Vector3f(-64.7F, 48.2F, -41.9F)
    };
    private final EinsteinSolarSystemModel<T> model;
    private final CustomHeadLayer<T, EinsteinSolarSystemModel<T>> headLayer;
    private final EinsteinSolarSystemArmorLayer<T, EinsteinSolarSystemModel<T>, HumanoidModel<T>> armorLayer;
    private final PartyHatLayer<T, EinsteinSolarSystemModel<T>> partyHatLayer;

    public EinsteinSolarSystemLayer(RenderLayerParent<T, V> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        model = new EinsteinSolarSystemModel<>(context.bakeLayer(EinsteinSolarSystemModel.MODEL_LAYER));
        headLayer = new CustomHeadLayer<>(this, context.getModelSet());
        armorLayer = new EinsteinSolarSystemArmorLayer<>(this,
                new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                context.getEquipmentRenderer()
        );

        if (PartyHatLayer.isModBirthday(false)) {
            partyHatLayer = new PartyHatLayer<>(this, context);
            return;
        }
        partyHatLayer = null;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T renderState, float yRot, float xRot) {
        EntityRenderStateAccessor accessor = (EntityRenderStateAccessor) renderState;
        if (shouldRender(accessor)) {
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
                headLayer.render(poseStack, bufferSource, packedLight, renderState, yRot, xRot);
                armorLayer.render(poseStack, bufferSource, packedLight, renderState, yRot, xRot);

                if (partyHatLayer != null) {
                    partyHatLayer.render(poseStack, bufferSource, packedLight, renderState, yRot, xRot);
                }

                poseStack.popPose();
                poseStack.popPose();
            }
        }
    }

    @Override
    public EinsteinSolarSystemModel<T> getModel() {
        return model;
    }

    public static boolean shouldRender(EntityRenderStateAccessor accessor) {
        return ModConfigs.GENERAL.enableEasterEggs
                && (Util.isMincraftEinstein(accessor.subtleEffects$get(STRING_UUID)) || Services.PLATFORM.isDevelopmentEnvironment())
                && !accessor.subtleEffects$get(IS_INVISIBLE);
    }

    public static float getSpin(EntityRenderStateAccessor accessor, float speed) {
        return accessor.subtleEffects$get(SOLAR_SYSTEM_SPIN) + (Mth.PI * speed);
    }
}
