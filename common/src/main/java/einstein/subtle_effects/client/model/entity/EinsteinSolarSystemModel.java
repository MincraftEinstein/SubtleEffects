package einstein.subtle_effects.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.SubtleEffects;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;

import java.util.List;

public class EinsteinSolarSystemModel<T extends AbstractClientPlayer> extends HumanoidModel<T> {

    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(SubtleEffects.loc("einstein_solar_system"), "main");

    public EinsteinSolarSystemModel(ModelPart rootPart) {
        super(rootPart);
        young = false;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        PartDefinition partDefinition = meshDefinition.getRoot();

        // Replaces head parts so that the pivot point is centered on the head
        partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -4, -4, 8, 8, 8), PartPose.ZERO);
        partDefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4, -4, -4, 8, 8, 8, new CubeDeformation(0.5F)), PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color) {
        head.render(poseStack, consumer, packedLight, packedOverlay);
        hat.render(poseStack, consumer, packedLight, packedOverlay);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return List.of();
    }

    @Override
    public void prepareMobModel(T player, float limbSwing, float limbSwingAmount, float partialTick) {
    }

    @Override
    public void setupAnim(T player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void copyPropertiesTo(HumanoidModel<T> model) {
        model.young = false;
        model.head.copyFrom(head);
        model.hat.copyFrom(hat);
    }

    @Override
    public void setAllVisible(boolean visible) {
        head.visible = visible;
        hat.visible = visible;
        body.visible = false;
        rightArm.visible = false;
        leftArm.visible = false;
        rightLeg.visible = false;
        leftLeg.visible = false;
    }
}
