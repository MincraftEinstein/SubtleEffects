package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class FlatPlaneParticle extends TextureSheetParticle {

    // Note: rotation is in radiens
    protected Quaternionf rotation = new Quaternionf();
    protected boolean renderBackFace = true;

    protected FlatPlaneParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    protected FlatPlaneParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, xo, this.x) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, yo, this.y) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, zo, this.z) - vec3.z());
        float quadSize = getQuadSize(partialTicks);
        float u0 = getU0();
        float u1 = getU1();
        float v0 = getV0();
        float v1 = getV1();
        int packedLight = getLightColor(partialTicks);

        if (roll != 0) {
            rotation.rotateZ(Mth.lerp(partialTicks, oRoll, roll));
        }

        renderVertex(consumer, x, y, z, -1, -1, quadSize, u1, v1, packedLight);
        renderVertex(consumer, x, y, z, -1, 1, quadSize, u1, v0, packedLight);
        renderVertex(consumer, x, y, z, 1, 1, quadSize, u0, v0, packedLight);
        renderVertex(consumer, x, y, z, 1, -1, quadSize, u0, v1, packedLight);

        if (renderBackFace) {
            renderVertex(consumer, x, y, z, 1, -1, quadSize, u0, v1, packedLight);
            renderVertex(consumer, x, y, z, 1, 1, quadSize, u0, v0, packedLight);
            renderVertex(consumer, x, y, z, -1, 1, quadSize, u1, v0, packedLight);
            renderVertex(consumer, x, y, z, -1, -1, quadSize, u1, v1, packedLight);
        }
    }

    private void renderVertex(VertexConsumer buffer, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = new Vector3f(xOffset, yOffset, 0).rotate(rotation).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(rCol, gCol, bCol, alpha).setLight(packedLight);
    }
}
