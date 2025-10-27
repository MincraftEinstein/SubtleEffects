package einstein.subtle_effects.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import einstein.subtle_effects.client.model.entity.EinsteinSolarSystemModel;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.EntityRenderStateAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import static einstein.subtle_effects.init.ModRenderStateKeys.SOLAR_SYSTEM_SPIN;
import static einstein.subtle_effects.init.ModRenderStateKeys.STRING_UUID;

public class EinsteinSolarSystemLayer<T extends AvatarRenderState, V extends HumanoidModel<T>> extends RenderLayer<T, V> implements RenderLayerParent<T, EinsteinSolarSystemModel<T>> {

    private static final Vector3f[] HEAD_ROTATIONS = {
            new Vector3f(27.6F, 61F, -9.7F),
            new Vector3f(2.5F, 8.6F, -13.8F),
            new Vector3f(-64.7F, 48.2F, -41.9F)
    };
    private final EinsteinSolarSystemModel<T> model;
    private final CustomHeadLayer<T, EinsteinSolarSystemModel<T>> headLayer;
    private final EinsteinSolarSystemArmorLayer<T, EinsteinSolarSystemModel<T>, HumanoidModel<T>> armorLayer;
    private final PartyHatLayer<T, V> partyHatLayer;

    @SuppressWarnings("unchecked")
    public EinsteinSolarSystemLayer(RenderLayerParent<?, ?> renderer, EntityRendererProvider.Context context) {
        super((RenderLayerParent<T, V>) renderer);
        // TODO fix the model copying head rotations
        model = new EinsteinSolarSystemModel<>(context.bakeLayer(EinsteinSolarSystemModel.MODEL_LAYER));
        headLayer = new CustomHeadLayer<>(this, context.getModelSet(), context.getPlayerSkinRenderCache());
        armorLayer = new EinsteinSolarSystemArmorLayer<>(this,
                ArmorModelSet.bake(ModelLayers.PLAYER_ARMOR, context.getModelSet(), EinsteinSolarSystemModel::new),
                context.getEquipmentRenderer()
        );

        if (PartyHatLayer.isModAnniversary()) {
            partyHatLayer = new PartyHatLayer<>(this, context);
            return;
        }
        partyHatLayer = null;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, T renderState, float yRot, float xRot) {
        EntityRenderStateAccessor accessor = (EntityRenderStateAccessor) renderState;
        if (!renderState.isInvisible && shouldRender(accessor)) {
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

                var renderType = RenderType.entityCutout(renderState.skin.body().texturePath());
                int packedOverlay = LivingEntityRenderer.getOverlayCoords(renderState, 0);
                model.setAllVisible(true);
                nodeCollector.submitModel(model, renderState, poseStack, renderType, packedLight, packedOverlay, renderState.outlineColor, null);

                poseStack.translate(0, 0.25, 0); // Adjusts the renders to align with the lower head model
                headLayer.submit(poseStack, nodeCollector, packedLight, renderState, yRot, xRot);
                armorLayer.submit(poseStack, nodeCollector, packedLight, renderState, yRot, xRot);

                if (partyHatLayer != null) {
                    partyHatLayer.submit(poseStack, nodeCollector, packedLight, renderState, yRot, xRot);
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
                && (Util.isMincraftEinstein(accessor.subtleEffects$get(STRING_UUID)) || Services.PLATFORM.isDevelopmentEnvironment());
    }

    public static float getSpin(EntityRenderStateAccessor accessor, float speed) {
        var spin = accessor.subtleEffects$get(SOLAR_SYSTEM_SPIN);
        return (spin != null) ? spin : 0 + (Mth.PI * speed);
    }
}
