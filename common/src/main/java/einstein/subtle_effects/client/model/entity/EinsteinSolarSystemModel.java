package einstein.subtle_effects.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import einstein.subtle_effects.SubtleEffects;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class EinsteinSolarSystemModel extends Model {

    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(SubtleEffects.loc("einstein_solar_system"), "main");
    private static final Vector3f[] HEAD_ROTATIONS = {
            new Vector3f(27.6F, 61F, -9.7F),
            new Vector3f(2.5F, 8.6F, -13.8F),
            new Vector3f(-64.7F, 48.2F, -41.9F)
    };
    private final ModelPart head;
    private final ModelPart hat;

    public EinsteinSolarSystemModel(ModelPart rootPart) {
        super(RenderType::entityCutout);
        head = rootPart.getChild("head");
        hat = rootPart.getChild("hat");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -4, -4, 8, 8, 8), PartPose.ZERO);
        partDefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4, -4, -4, 8, 8, 8, new CubeDeformation(0.5F)), PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float partialTicks, AbstractClientPlayer player) {
        int headCount = HEAD_ROTATIONS.length;

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

            head.render(poseStack, consumer, packedLight, packedOverlay);
            hat.render(poseStack, consumer, packedLight, packedOverlay);
            poseStack.popPose();
            poseStack.popPose();
        }
    }

    public static float getSpin(float partialTicks, AbstractClientPlayer player, float speed) {
        return (player.tickCount + partialTicks) / 20F + (Mth.PI * speed);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color) {
    }
}
