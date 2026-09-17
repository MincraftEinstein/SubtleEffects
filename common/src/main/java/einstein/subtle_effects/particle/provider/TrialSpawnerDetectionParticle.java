package einstein.subtle_effects.particle.provider;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

// Backported from 1.21
public class TrialSpawnerDetectionParticle extends TextureSheetParticle {

    private final SpriteSet sprites;
    private static final int BASE_LIFETIME = 8;

    protected TrialSpawnerDetectionParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float sizeMultiplier, SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);
        this.sprites = sprites;
        friction = 0.96F;
        gravity = -0.1F;
        speedUpWhenYMotionIsBlocked = true;
        xd *= 0;
        yd *= 0.9;
        zd *= 0;
        xd += xSpeed;
        yd += ySpeed;
        zd += zSpeed;
        quadSize *= 0.75F * sizeMultiplier;
        lifetime = (int) (8 / Mth.randomBetween(random, 0.5F, 1) * sizeMultiplier);
        lifetime = Math.max(lifetime, 1);
        setSpriteFromAge(sprites);
        hasPhysics = true;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float partialTick) {
        return Util.PARTICLE_LIGHT_COLOR;
    }

    public FacingCameraMode getFacingCameraMode() {
        return FacingCameraMode.LOOKAT_Y;
    }

    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Quaternionf quaternionf = new Quaternionf();
        getFacingCameraMode().setRotation(quaternionf, renderInfo, partialTicks);
        if (roll != 0.0F) {
            quaternionf.rotateZ(Mth.lerp(partialTicks, oRoll, roll));
        }

        renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
    }

    protected void renderRotatedQuad(VertexConsumer buffer, Camera camera, Quaternionf quaternion, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        float f = (float)(Mth.lerp(partialTicks, xo, x) - vec3.x());
        float f1 = (float)(Mth.lerp(partialTicks, yo, y) - vec3.y());
        float f2 = (float)(Mth.lerp(partialTicks, zo, z) - vec3.z());
        renderRotatedQuad(buffer, quaternion, f, f1, f2, partialTicks);
    }

    protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        float f = getQuadSize(partialTicks);
        float f1 = getU0();
        float f2 = getU1();
        float f3 = getV0();
        float f4 = getV1();
        int i = getLightColor(partialTicks);
        renderVertex(buffer, quaternion, x, y, z, 1.0F, -1.0F, f, f2, f4, i);
        renderVertex(buffer, quaternion, x, y, z, 1.0F, 1.0F, f, f2, f3, i);
        renderVertex(buffer, quaternion, x, y, z, -1.0F, 1.0F, f, f1, f3, i);
        renderVertex(buffer, quaternion, x, y, z, -1.0F, -1.0F, f, f1, f4, i);
    }

    private void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = (new Vector3f(xOffset, yOffset, 0.0F)).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.vertex(vector3f.x(), vector3f.y(), vector3f.z()).uv(u, v).color(rCol, gCol, bCol, alpha).uv2(packedLight);
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return quadSize * Mth.clamp(((float) age + scaleFactor) / (float) lifetime * 32, 0, 1);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new TrialSpawnerDetectionParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 1.5F, sprites);
        }
    }

    public interface FacingCameraMode {
        FacingCameraMode LOOKAT_XYZ = (p_312316_, p_311843_, p_312119_) -> p_312316_.set(p_311843_.rotation());
        FacingCameraMode LOOKAT_Y = (p_312695_, p_312346_, p_312064_) -> p_312695_.set(0.0F, p_312346_.rotation().y, 0.0F, p_312346_.rotation().w);

        void setRotation(Quaternionf var1, Camera var2, float var3);
    }
}
