package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.mixin.client.particle.TextureSheetParticleAccessor;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class BubbleParticleOverlayHandler {

    private final TextureSheetParticle particle;
    private TextureAtlasSprite overlaySprite;

    public BubbleParticleOverlayHandler(TextureSheetParticle particle) {
        this.particle = particle;
    }

    public void render(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        if (overlaySprite != null && Util.isBCWPPackLoaded()) {
            int lightColor = ((ParticleAccessor) particle).subtleEffects$getLightColor(partialTicks);
            float quadSize = particle.getQuadSize(partialTicks);
            float u0 = overlaySprite.getU0();
            float u1 = overlaySprite.getU1();
            float v0 = overlaySprite.getV0();
            float v1 = overlaySprite.getV1();

            renderVertex(buffer, quaternion, x, y, z, 1F, -1F, quadSize, u1, v1, lightColor);
            renderVertex(buffer, quaternion, x, y, z, 1F, 1F, quadSize, u1, v0, lightColor);
            renderVertex(buffer, quaternion, x, y, z, -1F, 1F, quadSize, u0, v0, lightColor);
            renderVertex(buffer, quaternion, x, y, z, -1F, -1F, quadSize, u0, v1, lightColor);
        }
    }

    private void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = (new Vector3f(xOffset, yOffset, 0F)).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(1, 1, 1, ((ParticleAccessor) particle).getAlpha()).setLight(packedLight);
    }

    public void setSprites(SpriteSet spriteSet) {
        List<TextureAtlasSprite> textures = Services.PARTICLE_HELPER.getSpritesFromSet(spriteSet);
        if (textures != null) {
            ((TextureSheetParticleAccessor) particle).setSpriteAccessor(textures.getFirst());
            overlaySprite = textures.get(1);
        }
    }
}
