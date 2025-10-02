package einstein.subtle_effects.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import einstein.subtle_effects.client.model.entity.PartyHatModel;
import einstein.subtle_effects.platform.Services;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static einstein.subtle_effects.SubtleEffects.loc;
import static einstein.subtle_effects.init.ModConfigs.GENERAL;

public class AnniversaryHatLayer<T extends AbstractClientPlayer, V extends HumanoidModel<T>> extends RenderLayer<T, V> {

    private static final List<ResourceLocation> TEXTURES = Util.make(new ArrayList<>(), textures -> {
        addTexture(textures, "blue");
        addTexture(textures, "red");
    });
    private final PartyHatModel<T> model;

    @SuppressWarnings("unchecked")
    public AnniversaryHatLayer(RenderLayerParent<?, ?> renderer, EntityRendererProvider.Context context) {
        super((RenderLayerParent<T, V>) renderer);
        model = new PartyHatModel<>(context.bakeLayer(PartyHatModel.MODEL_LAYER));
    }

    private static void addTexture(List<ResourceLocation> textures, String name) {
        textures.add(loc("textures/entity/party_hat/" + name + "_party_hat.png"));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (shouldRender(player)) {
            poseStack.pushPose();

            getParentModel().getHead().translateAndRotate(poseStack);

            float id = (float) Math.sin(player.getStringUUID().hashCode());
            poseStack.translate(0, -0.5, 0);
            poseStack.rotateAround(Axis.ZP.rotationDegrees(id * 22.5F), 0, 0.25F, 0);

            VertexConsumer consumer = bufferSource.getBuffer(model.renderType(getHatTexture(id)));
            model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);

            poseStack.popPose();
        }
    }

    private static ResourceLocation getHatTexture(float id) {
        return TEXTURES.get((int) (Mth.abs(id) * TEXTURES.size()));
    }

    public static boolean shouldRender(AbstractClientPlayer player) {
        return GENERAL.enableEasterEggs && !player.isInvisible();
    }

    public static boolean isModAnniversary() {
        if (Services.PLATFORM.isDevelopmentEnvironment()) {
            return true;
        }

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        return month == Calendar.OCTOBER && date >= 3 && date <= 5;
    }
}
