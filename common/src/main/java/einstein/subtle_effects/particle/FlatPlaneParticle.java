package einstein.subtle_effects.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import static einstein.subtle_effects.util.Util.radians;

public abstract class FlatPlaneParticle extends SingleQuadParticle {

    // Note: rotation is in radians
    protected Quaternionf rotation = new Quaternionf();
    protected boolean renderBackFace = true;

    protected FlatPlaneParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
    }

    protected FlatPlaneParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
    }

    @Override
    public void extract(QuadParticleRenderState state, Camera camera, float partialTicks) {
        Quaternionf rotation = new Quaternionf(this.rotation);
        if (roll != 0) {
            rotation.rotateZ(Mth.lerp(partialTicks, oRoll, roll));
        }

        renderQuad(state, camera, partialTicks, rotation);
    }

    protected void renderQuad(QuadParticleRenderState state, Camera camera, float partialTicks, Quaternionf rotation) {
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, xo, this.x) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, yo, this.y) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, zo, this.z) - vec3.z());

        renderQuad(state, rotation, partialTicks, x, y, z, false);

        if (renderBackFace) {
            renderQuad(state, rotation, partialTicks, x, y, z, true);
        }
    }

    protected void renderQuad(QuadParticleRenderState state, Quaternionf rotation, float partialTicks, float x, float y, float z, boolean renderInverted) {
        if (renderInverted) {
            rotation.rotateY(radians(-180));
        }
        state.add(
                getLayer(),
                x, y, z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                getQuadSize(partialTicks),
                getU0(), getU1(), getV0(), getV1(),
                ARGB.colorFromFloat(alpha, rCol, gCol, bCol),
                getLightColor(partialTicks)
        );
    }
}
