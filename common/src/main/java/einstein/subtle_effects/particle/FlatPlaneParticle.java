package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public abstract class FlatPlaneParticle extends TextureSheetParticle {

    // Note: rotation is in radiens
    protected Vector3f rotation = new Vector3f();

    protected FlatPlaneParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    protected FlatPlaneParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTick) {
        Vec3 pos = camera.getPosition();
        double x = Mth.lerp(partialTick, xo, this.x) - pos.x();
        double y = Mth.lerp(partialTick, yo, this.y) - pos.y();
        double z = Mth.lerp(partialTick, zo, this.z) - pos.z();
        Vec3[] vertexes = {
                new Vec3(-1, -1, 0),
                new Vec3(-1, 1, 0),
                new Vec3(1, 1, 0),
                new Vec3(1, -1, 0)
        };

        for (int i = 0; i < vertexes.length; i++) {
            vertexes[i] = vertexes[i]
                    .xRot(rotation.x())
                    .yRot(rotation.y())
                    .zRot(rotation.z())
                    .scale(getQuadSize(partialTick))
                    .add(x, y, z);
        }

        float minU = getU0();
        float maxU = getU1();
        float minV = getV0();
        float maxV = getV1();
        int lightColor = getLightColor(partialTick);

        consumer.addVertex((float) vertexes[0].x(), (float) vertexes[0].y(), (float) vertexes[0].z())
                .setUv(maxU, maxV)
                .setColor(rCol, gCol, bCol, alpha)
                .setLight(lightColor);

        consumer.addVertex((float) vertexes[1].x(), (float) vertexes[1].y(), (float) vertexes[1].z())
                .setUv(maxU, minV)
                .setColor(rCol, gCol, bCol, alpha)
                .setLight(lightColor);

        consumer.addVertex((float) vertexes[2].x(), (float) vertexes[2].y(), (float) vertexes[2].z())
                .setUv(minU, minV)
                .setColor(rCol, gCol, bCol, alpha)
                .setLight(lightColor);

        consumer.addVertex((float) vertexes[3].x(), (float) vertexes[3].y(), (float) vertexes[3].z())
                .setUv(minU, maxV)
                .setColor(rCol, gCol, bCol, alpha)
                .setLight(lightColor);
    }
}
