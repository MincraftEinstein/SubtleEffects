package einstein.subtle_effects.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.SubtleEffects;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;

public class PartyHatModel<T extends AbstractClientPlayer> extends EntityModel<T> {

    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(SubtleEffects.loc("party_hat"), "main");
    private final ModelPart hat;

    public PartyHatModel(ModelPart rootPart) {
        hat = rootPart.getChild("hat");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition hat = partDefinition.addOrReplaceChild("hat_1", CubeListBuilder.create().texOffs(0, 4).addBox(-6, -8, -1, 7, 8, 0, CubeDeformation.NONE), PartPose.offset(2.5F, 0, 1));
        hat.addOrReplaceChild("fuzz_1", CubeListBuilder.create().texOffs(0, 0).addBox(-2, -2, 0, 4, 4, 0, CubeDeformation.NONE), PartPose.offsetAndRotation(-2.5F, -10, -1, 0, 0.7854F, 0));
        hat.addOrReplaceChild("fuzz_2", CubeListBuilder.create().texOffs(0, 0).addBox(-2, -2, 0, 4, 4, 0, CubeDeformation.NONE), PartPose.offsetAndRotation(-2.5F, -10, -1, 0, -0.7854F, 0));
        hat.addOrReplaceChild("hat_2", CubeListBuilder.create().texOffs(0, 4).addBox(-3.5F, -4, 0, 7, 8, 0, CubeDeformation.NONE), PartPose.offsetAndRotation(-2.5F, -4, -1, 0, -1.5708F, 0));
        return LayerDefinition.create(meshDefinition, 16, 16);
    }

    @Override
    public void setupAnim(T player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color) {
        hat.render(poseStack, consumer, packedLight, packedOverlay);
    }
}
