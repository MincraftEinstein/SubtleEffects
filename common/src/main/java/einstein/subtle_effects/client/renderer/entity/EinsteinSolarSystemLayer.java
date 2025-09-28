package einstein.subtle_effects.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import einstein.subtle_effects.client.model.entity.EinsteinSolarSystemModel;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.platform.Services;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.joml.Vector3f;

public class EinsteinSolarSystemLayer<T extends AbstractClientPlayer, V extends PlayerModel<T>> extends RenderLayer<T, V> {

    private static final String UUID = "d71e4b41-9315-499f-a934-ca925421fb38";
    private static final Vector3f[] HEAD_ROTATIONS = {
            new Vector3f(27.6F, 61F, -9.7F),
            new Vector3f(2.5F, 8.6F, -13.8F),
            new Vector3f(-64.7F, 48.2F, -41.9F)
    };
    private final EinsteinSolarSystemRenderLayerParentImpl<T, V> renderLayerParent;
    private final EinsteinSolarSystemModel<T> model;
    private final CustomHeadLayer<T, EinsteinSolarSystemModel<T>> headLayer;
    private final EinsteinSolarSystemArmorLayer<T, EinsteinSolarSystemModel<T>, HumanoidModel<T>> armorLayer;
    private final AnniversaryHatLayer<T, V> anniversaryHatLayer;

    @SuppressWarnings("unchecked")
    public EinsteinSolarSystemLayer(RenderLayerParent<?, ?> renderer, EntityRendererProvider.Context context) {
        super((RenderLayerParent<T, V>) renderer);
        model = new EinsteinSolarSystemModel<>(context.bakeLayer(EinsteinSolarSystemModel.MODEL_LAYER));
        renderLayerParent = new EinsteinSolarSystemRenderLayerParentImpl<>(this);
        headLayer = new CustomHeadLayer<>(renderLayerParent, context.getModelSet(), context.getItemInHandRenderer());
        armorLayer = new EinsteinSolarSystemArmorLayer<>(renderLayerParent,
                new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                context.getModelManager()
        );

        if (AnniversaryHatLayer.isModAnniversary()) {
            anniversaryHatLayer = new AnniversaryHatLayer<>(renderLayerParent, context);
            return;
        }
        anniversaryHatLayer = null;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (shouldRender(player)) {
            int headCount = HEAD_ROTATIONS.length;
            model.hat.visible = player.isModelPartShown(PlayerModelPart.HAT);

            for (int i = 0; i < headCount; i++) {
                float i1 = i + 1;
                float spin = getSpin(partialTicks, player, i) * (headCount / i1);
                Vector3f rotation = HEAD_ROTATIONS[i];

                poseStack.pushPose();
                poseStack.mulPose(Axis.YP.rotationDegrees(-180 - Mth.rotLerp(partialTicks, player.yBodyRotO, player.yBodyRot)));
                poseStack.mulPose(Axis.YP.rotation(spin)); // Spins the head around the player

                poseStack.translate(0.7 * i1, player.isCrouching() ? 0 : -0.25, 0);
                poseStack.scale(0.8F, 0.8F, 0.8F);

                poseStack.pushPose();
                poseStack.mulPose(Axis.XP.rotationDegrees(rotation.x()));
                poseStack.mulPose(Axis.YP.rotationDegrees(rotation.y()));
                poseStack.mulPose(Axis.ZP.rotationDegrees(rotation.z()));

                poseStack.mulPose(Axis.YP.rotation(spin)); // Spins the head itself

                VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(getTextureLocation(player)));
                model.renderToBuffer(poseStack, consumer, packedLight, LivingEntityRenderer.getOverlayCoords(player, 0), -1);

                poseStack.translate(0, 0.25, 0); // Adjusts the renders to align with the lower head model
                headLayer.render(poseStack, bufferSource, packedLight, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                armorLayer.render(poseStack, bufferSource, packedLight, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);

                if (anniversaryHatLayer != null) {
                    anniversaryHatLayer.render(poseStack, bufferSource, packedLight, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }

                poseStack.popPose();
                poseStack.popPose();
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(T player) {
        return super.getTextureLocation(player);
    }

    public EinsteinSolarSystemModel<T> getModel() {
        return model;
    }

    public EinsteinSolarSystemRenderLayerParentImpl<T, V> getRenderLayerParent() {
        return renderLayerParent;
    }

    public static boolean shouldRender(AbstractClientPlayer player) {
        return ModConfigs.GENERAL.enableEasterEggs
                && (player.getStringUUID().equals(UUID) || Services.PLATFORM.isDevelopmentEnvironment())
                && !player.isInvisible();
    }

    public static float getSpin(float partialTicks, AbstractClientPlayer player, float speed) {
        return (player.tickCount + partialTicks) / 20F + (Mth.PI * speed);
    }
}
