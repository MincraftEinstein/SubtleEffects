package einstein.subtle_effects.mixin.client.particle.bubbles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.BubbleSetter;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin({BubbleParticle.class, BubbleColumnUpParticle.class, WaterCurrentDownParticle.class})
public abstract class BubbleParticleMixin extends TextureSheetParticle implements BubbleSetter {

    @Unique
    private TextureAtlasSprite subtleEffects$overlaySprite;

    @Unique
    private boolean subtleEffects$playsSound;

    @Unique
    private int subtleEffects$waterColor;

    protected BubbleParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        BlockPos pos = BlockPos.containing(x, y, z);
        subtleEffects$waterColor = level.getBiome(pos).value().getWaterColor();

        if (!isAlive()) {
            float volume = ModConfigs.GENERAL.poppingBubblesVolume.get();

            if (subtleEffects$playsSound && volume > 0) {
                if (!level.isWaterAt(pos)) {
                    level.playLocalSound(x, y, z, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.AMBIENT,
                            Mth.nextInt(random, 1, 4) * volume,
                            Mth.nextFloat(random, 1, 1.3F), false
                    );
                }
            }

            if (ModConfigs.GENERAL.poppingBubbles) {
                level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, xd, yd, zd);
            }
        }
    }

    @Override
    protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        super.renderRotatedQuad(buffer, quaternion, x, y, z, partialTicks);
        if (subtleEffects$overlaySprite != null) {
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
        float colorIntensity = 0.20F;
        float whiteIntensity = 1 - colorIntensity;

        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v)
                .setColor(whiteIntensity + (colorIntensity * ((subtleEffects$waterColor >> 16 & 255) / 255F)),
                        whiteIntensity + (colorIntensity * ((subtleEffects$waterColor >> 8 & 255) / 255F)),
                        whiteIntensity + (colorIntensity * ((subtleEffects$waterColor & 255) / 255F)), alpha)
                .setLight(packedLight);
    }

    @Override
    public void subtleEffects$setupBubble(SpriteSet sprites, boolean playsSound) {
        List<TextureAtlasSprite> textures = Services.PARTICLE_HELPER.getSpritesFromSet(sprites);
        subtleEffects$playsSound = playsSound;

        if (textures != null && textures.size() > 1 && Util.isBCWPPackLoaded()) {
            setSprite(textures.getFirst());
            subtleEffects$overlaySprite = textures.get(1);
        }
    }
}
