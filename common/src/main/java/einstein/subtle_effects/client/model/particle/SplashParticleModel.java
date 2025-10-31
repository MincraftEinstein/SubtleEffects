package einstein.subtle_effects.client.model.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.init.ModRenderTypes;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Unit;

public class SplashParticleModel extends Model<Unit> {

    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(SubtleEffects.loc("splash_particle"), "main");

    public SplashParticleModel(ModelPart rootPart) {
        super(rootPart, ModRenderTypes::entityParticleTranslucent);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("splash", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-6, -24, -6, 12, 24, 12, CubeDeformation.NONE),
                PartPose.offset(0, 24, 0)
        );
        return LayerDefinition.create(meshdefinition, 48, 48);
    }
}
