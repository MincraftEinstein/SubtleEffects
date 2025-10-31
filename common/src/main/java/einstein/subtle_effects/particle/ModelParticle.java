package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public abstract class ModelParticle extends Particle {

    protected final Minecraft minecraft = Minecraft.getInstance();

    protected ModelParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    public ModelParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        PoseStack poseStack = new PoseStack();
        Vec3 cameraPos = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, xo, this.x) - cameraPos.x());
        float y = (float) (Mth.lerp(partialTicks, yo, this.y) - cameraPos.y());
        float z = (float) (Mth.lerp(partialTicks, zo, this.z) - cameraPos.z());

        poseStack.translate(x, y, z);
        poseStack.scale(1, -1, -1);

        render(poseStack, consumer, bufferSource, camera, partialTicks);
    }

    protected abstract void render(PoseStack poseStack, VertexConsumer consumer, MultiBufferSource.BufferSource bufferSource, Camera camera, float partialTicks);

    protected <T extends Model> T bakeModel(Function<ModelPart, T> modelBaker, ModelLayerLocation layerLocation) {
        return modelBaker.apply(minecraft.getEntityModels().bakeLayer(layerLocation));
    }

    protected ResourceLocation getSpriteId(TextureAtlasSprite sprite) {
        return sprite.contents().name().withPrefix("textures/particle/").withSuffix(".png");
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
}
