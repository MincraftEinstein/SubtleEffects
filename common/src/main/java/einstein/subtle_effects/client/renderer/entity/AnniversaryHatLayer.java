package einstein.subtle_effects.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.platform.Services;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.level.block.Blocks;

import java.util.Calendar;

import static einstein.subtle_effects.init.ModConfigs.GENERAL;

public class AnniversaryHatLayer<T extends AbstractClientPlayer, V extends HumanoidModel<T>> extends RenderLayer<T, V> {

    private final BlockRenderDispatcher blockRenderDispatcher;

    @SuppressWarnings("unchecked")
    public AnniversaryHatLayer(RenderLayerParent<?, ?> renderer, EntityRendererProvider.Context context) {
        super((RenderLayerParent<T, V>) renderer);
        blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (shouldRender(player)) {
            poseStack.pushPose();
            getParentModel().getHead().translateAndRotate(poseStack);

            poseStack.translate(-0.5, -1.5, -0.5);
            blockRenderDispatcher.renderSingleBlock(Blocks.LIGHTNING_ROD.defaultBlockState(), poseStack, bufferSource, packedLight, LivingEntityRenderer.getOverlayCoords(player, 0));
            poseStack.popPose();
        }
    }

    public static boolean shouldRender(AbstractClientPlayer player) {
        return (GENERAL.enableEasterEggs) && !player.isInvisible();
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
