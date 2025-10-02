package einstein.subtle_effects.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.SubtleEffects;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;

public class PartyHatModel<T extends AbstractClientPlayer> extends EntityModel<T> {

    ModelPart hat;

    public PartyHatModel(ModelPart rootPart) {
        super();
        hat = rootPart.getChild("hat");
    }

    @Override
    public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
        hat.render(poseStack, vertexConsumer, i, i1, i2);
    }

    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(SubtleEffects.loc("party_hat"), "main");

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        PartDefinition root = mesh.getRoot();

        var def =new  CubeDeformation(0f);
        var hat = root.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 4).addBox(-6.0F, -8.0F, -1.0F, 7.0F, 8.0F, 0.0F, def), PartPose.offset(2.5F, 0.0F, 1.0F));
        hat.addOrReplaceChild("fuzz_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, def), PartPose.offsetAndRotation(-2.5F, -10.0F, -1.0F, 0.0F, 0.7854F, 0.0F));
        hat.addOrReplaceChild("fuzz_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, def), PartPose.offsetAndRotation(-2.5F, -10.0F, -1.0F, 0.0F, -0.7854F, 0.0F));
        hat.addOrReplaceChild("base_r1", CubeListBuilder.create().texOffs(0, 4).addBox(-3.5F, -4.0F, 0.0F, 7.0F, 8.0F, 0.0F, def), PartPose.offsetAndRotation(-2.5F, -4.0F, -1.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(mesh, 16, 16);
    }
}
