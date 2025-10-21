package einstein.subtle_effects.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import einstein.subtle_effects.client.model.entity.PartyHatModel;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.EntityRenderStateAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static einstein.subtle_effects.SubtleEffects.loc;
import static einstein.subtle_effects.init.ModConfigs.GENERAL;
import static einstein.subtle_effects.init.ModRenderStateKeys.IS_INVISIBLE;
import static einstein.subtle_effects.init.ModRenderStateKeys.STRING_UUID;

public class PartyHatLayer<T extends PlayerRenderState, V extends HumanoidModel<T>> extends RenderLayer<T, V> {

    public static final List<ResourceLocation> TEXTURES = net.minecraft.Util.make(new ArrayList<>(), textures -> {
        addTexture(textures, "big_fuzz_0");
        addTexture(textures, "big_fuzz_1");
        addTexture(textures, "big_fuzz_2");
        addTexture(textures, "big_fuzz_3");
        addTexture(textures, "big_fuzz_4");
        addTexture(textures, "small_fuzz_0");
        addTexture(textures, "small_fuzz_1");
        addTexture(textures, "small_fuzz_2");
        addTexture(textures, "small_fuzz_3");
        addTexture(textures, "small_fuzz_4");
    });
    private final PartyHatModel<T> model;

    public PartyHatLayer(RenderLayerParent<T, V> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        model = new PartyHatModel<>(context.bakeLayer(PartyHatModel.MODEL_LAYER));
    }

    public static void addTexture(List<ResourceLocation> textures, String name) {
        textures.add(loc("textures/entity/party_hat/" + name + ".png"));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T renderState, float yRot, float xRot) {
        EntityRenderStateAccessor accessor = (EntityRenderStateAccessor) renderState;
        if (shouldRender(accessor)) {
            poseStack.pushPose();

            getParentModel().getHead().translateAndRotate(poseStack);

            String uuid = accessor.subtleEffects$get(STRING_UUID);
            float id = (float) Math.sin(uuid.hashCode());
            poseStack.translate(0, -0.5, 0);
            poseStack.rotateAround(Axis.ZP.rotationDegrees(id * 22.5F), 0, 0.25F, 0);

            VertexConsumer consumer = bufferSource.getBuffer(model.renderType(getHatTexture(id, uuid)));
            model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);

            poseStack.popPose();
        }
    }

    private static ResourceLocation getHatTexture(float id, String uuid) {
        if (Util.isMincraftEinstein(uuid)) {
            return TEXTURES.getFirst();
        }

        int index = (int) (Mth.abs(id) * TEXTURES.size());
        return TEXTURES.get(index >= TEXTURES.size() ? TEXTURES.size() - 1 : index);
    }

    public static boolean shouldRender(EntityRenderStateAccessor accessor) {
        return GENERAL.enableEasterEggs && !accessor.subtleEffects$get(IS_INVISIBLE);
    }

    public static boolean isModBirthday(boolean ignoreInDev) {
        if (!ignoreInDev && Services.PLATFORM.isDevelopmentEnvironment()) {
            return true;
        }

        LocalDate date = LocalDate.now();
        Month month = date.getMonth();
        int dayOfMonth = date.getDayOfMonth();
        return month == Month.OCTOBER && dayOfMonth >= 3 && dayOfMonth <= 5;
    }
}
