package einstein.subtle_effects.mixin.client.particle.bubbles;

import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.DrowningBubbleParticle;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.BubbleSetter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin({BubbleParticle.class, BubbleColumnUpParticle.class, WaterCurrentDownParticle.class})
public abstract class BubbleParticleMixin extends SingleQuadParticle implements BubbleSetter {

    @Unique
    private TextureAtlasSprite subtleEffects$overlaySprite;

    @Unique
    private boolean subtleEffects$playsSound;

    @Unique
    private int subtleEffects$waterColor;

    protected BubbleParticleMixin(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
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
                if (((SingleQuadParticle) this) instanceof DrowningBubbleParticle) {
                    level.addParticle(ModParticles.DROWNING_BUBBLE_POP.get(), x, y, z, xd, yd, zd);
                    return;
                }

                level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, xd, yd, zd);
            }
        }
    }

    @Override
    protected void extractRotatedQuad(QuadParticleRenderState state, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        super.extractRotatedQuad(state, quaternion, x, y, z, partialTicks);
        if (subtleEffects$overlaySprite != null) {
            float u0 = subtleEffects$overlaySprite.getU0();
            float u1 = subtleEffects$overlaySprite.getU1();
            float v0 = subtleEffects$overlaySprite.getV0();
            float v1 = subtleEffects$overlaySprite.getV1();
            state.add(
                    getLayer(), x, y, z,
                    quaternion.x, quaternion.y, quaternion.z, quaternion.w,
                    getQuadSize(partialTicks), u0, u1, v0, v1,
                    subtleEffects$color(), getLightColor(partialTicks)
            );
        }
    }

    @Unique
    private int subtleEffects$color() {
        float colorIntensity = 0.20F;
        float whiteIntensity = 1 - colorIntensity;

        return ARGB.colorFromFloat(whiteIntensity + (colorIntensity * ((subtleEffects$waterColor >> 16 & 255) / 255F)),
                whiteIntensity + (colorIntensity * ((subtleEffects$waterColor >> 8 & 255) / 255F)),
                whiteIntensity + (colorIntensity * ((subtleEffects$waterColor & 255) / 255F)), alpha);
    }

    @Override
    public void subtleEffects$setupBubble(SpriteSet sprites, boolean playsSound) {
        List<TextureAtlasSprite> textures = Services.PARTICLE_HELPER.getSpritesFromSet(sprites);
        subtleEffects$playsSound = playsSound;

        if (textures != null && textures.size() > 1 && BCWPPackManager.isPackLoaded()) {
            setSprite(textures.getFirst());
            subtleEffects$overlaySprite = textures.get(1);
        }
    }
}
