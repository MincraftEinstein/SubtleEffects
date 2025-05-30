package einstein.subtle_effects.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.client.model.entity.EinsteinSolarSystemModel;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.EntityAccessRenderState;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.entity.Entity;

public class EinsteinSolarSystemLayer extends RenderLayer<PlayerRenderState, PlayerModel> {

    private static final String UUID = "d71e4b41-9315-499f-a934-ca925421fb38";
    private final EinsteinSolarSystemModel model;

    @SuppressWarnings("unchecked")
    public EinsteinSolarSystemLayer(RenderLayerParent<?, ?> renderer, EntityRendererProvider.Context context) {
        super((RenderLayerParent<PlayerRenderState, PlayerModel>) renderer);
        model = new EinsteinSolarSystemModel(context.bakeLayer(EinsteinSolarSystemModel.MODEL_LAYER));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, PlayerRenderState renderState, float xRot, float yRot) {
        EntityAccessRenderState accessor = (EntityAccessRenderState) renderState;
        Entity entity = accessor.subtleEffects$getEntity();
        float partialTick = accessor.getPartialTick();

        if (entity instanceof AbstractClientPlayer player) {
            if (shouldRender(player)) {
                VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(player.getSkin().texture()));
                model.render(poseStack, consumer, packedLight, LivingEntityRenderer.getOverlayCoords(renderState, 0), partialTick, player);
            }
        }
    }

    public static boolean shouldRender(AbstractClientPlayer player) {
        return ModConfigs.GENERAL.enableEasterEggs
                && (player.getStringUUID().equals(UUID) || Services.PLATFORM.isDevelopmentEnvironment())
                && !player.isInvisible();
    }
}
