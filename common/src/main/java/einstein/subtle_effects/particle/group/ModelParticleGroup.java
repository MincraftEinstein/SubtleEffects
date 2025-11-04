package einstein.subtle_effects.particle.group;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;

import einstein.subtle_effects.particle.ModelParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.model.Model;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.state.ParticleGroupRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelParticleGroup extends ParticleGroup<ModelParticle<?>> {
    public ModelParticleGroup(ParticleEngine engine) {
        super(engine);
    }

    public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTick) {
        return new State(this.particles.stream().map((p_445817_) -> ModelParticleRenderState.fromParticle(p_445817_, camera, partialTick)).toList());
    }

    @OnlyIn(Dist.CLIENT)
    static record ModelParticleRenderState(Model<Unit> model, PoseStack poseStack, RenderType renderType, int color) {
        public static ModelParticleRenderState fromParticle(ModelParticle<?> particle, Camera camera, float partialTick) {
            float f = ((float)particle.age + partialTick) / (float)particle.lifetime;
            float f1 = 0.05F + 0.5F * Mth.sin(f * (float)Math.PI);
            int i = ARGB.colorFromFloat(f1, 1.0F, 1.0F, 1.0F);
            PoseStack posestack = new PoseStack();
            posestack.pushPose();
            posestack.mulPose(camera.rotation());
            posestack.mulPose(Axis.XP.rotationDegrees(60.0F - 150.0F * f));
            float f2 = 0.42553192F;
            posestack.scale(0.42553192F, -0.42553192F, -0.42553192F);
            posestack.translate(0.0F, -0.56F, 3.5F);
            return new ModelParticleRenderState(particle.getModel(), posestack, particle.renderType, i);
        }
    }

    @OnlyIn(Dist.CLIENT)
    static record State(List<ModelParticleRenderState> states) implements ParticleGroupRenderState {
        public void submit(SubmitNodeCollector collector, CameraRenderState cameraState) {
            for(ModelParticleRenderState state : this.states) {
                collector.submitModel(state.model, Unit.INSTANCE, state.poseStack, state.renderType, 15728880, OverlayTexture.NO_OVERLAY, state.color, (TextureAtlasSprite)null, 0, (ModelFeatureRenderer.CrumblingOverlay)null);
            }

        }
    }
}