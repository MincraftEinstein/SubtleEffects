package einstein.subtle_effects.mixin.client.particle.bubbles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.SpriteSetSetter;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin({BubbleParticle.class, BubbleColumnUpParticle.class, WaterCurrentDownParticle.class})
public abstract class BubbleParticleMixin extends TextureSheetParticle implements SpriteSetSetter {

    @Unique
    private TextureAtlasSprite subtleEffects$overlaySprite;

    protected BubbleParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        super.render(buffer, camera, partialTicks);
        if (subtleEffects$overlaySprite != null && Util.isBCWPPackLoaded()) {
            int lightColor = getLightColor(partialTicks);
            float quadSize = getQuadSize(partialTicks);
            float u0 = subtleEffects$overlaySprite.getU0();
            float u1 = subtleEffects$overlaySprite.getU1();
            float v0 = subtleEffects$overlaySprite.getV0();
            float v1 = subtleEffects$overlaySprite.getV1();

            Vec3 cameraPos = camera.getPosition();
            float x = (float) (Mth.lerp(partialTicks, xo, this.x) - cameraPos.x());
            float y = (float) (Mth.lerp(partialTicks, yo, this.y) - cameraPos.y());
            float z = (float) (Mth.lerp(partialTicks, zo, this.z) - cameraPos.z());

            Quaternionf quaternion;
            if (roll == 0) {
                quaternion = camera.rotation();
            }
            else {
                quaternion = new Quaternionf(camera.rotation());
                quaternion.rotateZ(Mth.lerp(partialTicks, oRoll, roll));
            }

            subtleEffects$renderVertex(buffer, quaternion, x, y, z, -1, -1, quadSize, u0, v1, lightColor);
            subtleEffects$renderVertex(buffer, quaternion, x, y, z, -1, 1, quadSize, u0, v0, lightColor);
            subtleEffects$renderVertex(buffer, quaternion, x, y, z, 1, 1, quadSize, u1, v0, lightColor);
            subtleEffects$renderVertex(buffer, quaternion, x, y, z, 1, -1, quadSize, u1, v1, lightColor);
        }
    }

    @Unique
    private void subtleEffects$renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = (new Vector3f(xOffset, yOffset, 0)).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.vertex(vector3f.x(), vector3f.y(), vector3f.z()).uv(u, v).color(1, 1, 1, alpha).uv2(packedLight).endVertex();
    }

    @Override
    public void subtleEffects$setSpriteSet(SpriteSet sprites) {
        List<TextureAtlasSprite> textures = Services.PARTICLE_HELPER.getSpritesFromSet(sprites);
        if (textures != null && textures.size() > 1 && Util.isBCWPPackLoaded()) {
            setSprite(textures.get(0));
            subtleEffects$overlaySprite = textures.get(1);
        }
    }
}
