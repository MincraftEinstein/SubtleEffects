package einstein.subtle_effects.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import einstein.subtle_effects.client.model.entity.PartyHatModel;
import einstein.subtle_effects.platform.Services;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.Calendar;

import static einstein.subtle_effects.SubtleEffects.loc;
import static einstein.subtle_effects.init.ModConfigs.GENERAL;
import static java.lang.Math.sin;

public class AnniversaryHatLayer<T extends AbstractClientPlayer, V extends HumanoidModel<T>> extends RenderLayer<T, V> {

    EntityRenderDispatcher dispatcher;
    private final PartyHatModel<T> model;

    @SuppressWarnings("unchecked")
    public AnniversaryHatLayer(RenderLayerParent<?, ?> renderer, EntityRendererProvider.Context context) {
        super((RenderLayerParent<T, V>) renderer);
        dispatcher = context.getEntityRenderDispatcher();
        model = new PartyHatModel<>(context.bakeLayer(PartyHatModel.MODEL_LAYER));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (shouldRender(player)) {
            poseStack.pushPose();
            getParentModel().getHead().translateAndRotate(poseStack);

            poseStack.translate(0f, -8f * PIXEL, 0f);
            var id = (float) sin(player.getStringUUID().hashCode());

            poseStack.rotateAround(Axis.ZP.rotationDegrees(id * 22.5f), 0f, 4f * PIXEL, 0f);

            var buffer = bufferSource.getBuffer(model.renderType(getHatTexture(id + 0.5f)));
            model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 0xffffff);
            poseStack.popPose();
        }
    }

    public static float PIXEL = 1f / 16f;

    public ResourceLocation getHatTexture(float id) {
        var tex = (id > 0.5) ? "red" : "blue";
        return loc("textures/misc/party_hat/%s.png".formatted(tex));
    }


    public static boolean shouldRender(AbstractClientPlayer player) {
        return (GENERAL.enableEasterEggs) && !player.isInvisible();
    }

    @Override
    protected ResourceLocation getTextureLocation(T entity) {
        return super.getTextureLocation(entity);
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
