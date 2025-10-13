package einstein.subtle_effects.client.model.entity;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public class EinsteinSolarSystemModel<T extends AvatarRenderState> extends HumanoidModel<T> {

    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(SubtleEffects.loc("einstein_solar_system"), "main");

    public EinsteinSolarSystemModel(ModelPart rootPart) {
        super(rootPart);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        PartDefinition partDefinition = meshDefinition.getRoot();

        // Replaces head parts so that the pivot point is centered on the head
        PartDefinition headDefinition = partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -4, -4, 8, 8, 8), PartPose.ZERO);
        headDefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4, -4, -4, 8, 8, 8, new CubeDeformation(0.5F)), PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void copyPropertiesTo(HumanoidModel<T> model) {
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
