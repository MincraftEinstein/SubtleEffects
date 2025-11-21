package einstein.subtle_effects.particle.group;

import com.mojang.blaze3d.vertex.PoseStack;
import einstein.subtle_effects.particle.ModelParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.model.Model;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.state.ParticleGroupRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Unit;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModelParticleGroup extends ParticleGroup<ModelParticle<?>> {

    public ModelParticleGroup(ParticleEngine engine) {
        super(engine);
    }

    public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTick) {
        return new State(particles.stream().map((particle) -> particle.extractState(camera, partialTick)).toList());
    }

    public record ModelParticleRenderState(Model<Unit> model, PoseStack poseStack, RenderType renderType,
                                           int lightColor, int color, @Nullable OverlayModelState overlay) {

    }

    public record OverlayModelState(int color, RenderType renderType) {

    }

    public record State(List<ModelParticleRenderState> states) implements ParticleGroupRenderState {

        public void submit(SubmitNodeCollector collector, CameraRenderState cameraState) {
            for (ModelParticleRenderState state : states) {
                collector.submitModel(state.model, Unit.INSTANCE, state.poseStack, state.renderType, state.lightColor, OverlayTexture.NO_OVERLAY, state.color, null, 0, null);

                if (state.overlay != null) {
                    collector.submitModel(state.model, Unit.INSTANCE, state.poseStack, state.overlay.renderType, state.lightColor, OverlayTexture.NO_OVERLAY, state.overlay.color, null, 0, null);
                }
            }
        }
    }
}