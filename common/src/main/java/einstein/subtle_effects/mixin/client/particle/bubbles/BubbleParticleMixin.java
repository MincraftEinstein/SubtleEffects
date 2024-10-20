package einstein.subtle_effects.mixin.client.particle.bubbles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.SpriteSetSetter;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
    protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        super.renderRotatedQuad(buffer, quaternion, x, y, z, partialTicks);
        if (subtleEffects$overlaySprite != null && Util.isBCWPPackLoaded()) {
            int lightColor = getLightColor(partialTicks);
            float quadSize = getQuadSize(partialTicks);
            float u0 = subtleEffects$overlaySprite.getU0();
            float u1 = subtleEffects$overlaySprite.getU1();
            float v0 = subtleEffects$overlaySprite.getV0();
            float v1 = subtleEffects$overlaySprite.getV1();

            subtleEffects$renderVertex(buffer, quaternion, x, y, z, 1, -1, quadSize, u1, v1, lightColor);
            subtleEffects$renderVertex(buffer, quaternion, x, y, z, 1, 1, quadSize, u1, v0, lightColor);
            subtleEffects$renderVertex(buffer, quaternion, x, y, z, -1, 1, quadSize, u0, v0, lightColor);
            subtleEffects$renderVertex(buffer, quaternion, x, y, z, -1, -1, quadSize, u0, v1, lightColor);
        }
    }

    @Unique
    private void subtleEffects$renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = (new Vector3f(xOffset, yOffset, 0)).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(1, 1, 1, alpha).setLight(packedLight);
    }

    @Override
    public void subtleEffects$setSpriteSet(SpriteSet sprites) {
        List<TextureAtlasSprite> textures = Services.PARTICLE_HELPER.getSpritesFromSet(sprites);
        if (textures != null && Util.isBCWPPackLoaded()) {
            setSprite(textures.getFirst());
            subtleEffects$overlaySprite = textures.get(1);
        }
    }
}
