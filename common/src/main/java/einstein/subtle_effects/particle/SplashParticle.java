package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.client.model.particle.SplashParticleModel;
import einstein.subtle_effects.particle.option.SplashParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;
import static einstein.subtle_effects.init.ModSpriteSets.WATER_SPLASH_OVERLAY;

public class SplashParticle extends ModelParticle {

    private final boolean glowing;
    private final TagKey<Fluid> fluidTag;
    private final SpriteSet sprites;
    @Nullable
    private final SpriteSet overlaySprites;
    private final boolean hasOverlay;
    private final BlockPos.MutableBlockPos pos;
    private float xScale;
    private final float yScale;
    private ResourceLocation texture;
    private ResourceLocation overlayTexture;
    private float overlayRCol = 1;
    private float overlayGCol = 1;
    private float overlayBCol = 1;
    private final SplashParticleModel model;

    protected SplashParticle(ClientLevel level, double x, double y, double z, boolean glowing, TagKey<Fluid> fluidTag, SpriteSet sprites, @Nullable SpriteSet overlaySprites, SplashParticleOptions options) {
        super(level, x, y, z);
        model = bakeModel(SplashParticleModel::new, SplashParticleModel.MODEL_LAYER);
        this.glowing = glowing;
        this.fluidTag = fluidTag;
        this.sprites = sprites;
        this.overlaySprites = overlaySprites;
        pos = BlockPos.containing(x, y, z).mutable();
        hasOverlay = overlaySprites != null && ENTITIES.splashes.splashOverlayAlpha.get() > 0;
        lifetime = 15;
        xScale = options.xScale();
        yScale = options.yScale();
        setSize(xScale, yScale);
        setSpriteFromAge();
    }

    @Override
    protected int getLightColor(float partialTick) {
        if (glowing) {
            return 240;
        }
        return super.getLightColor(partialTick);
    }

    @Override
    public void tick() {
        pos.set(x, y, z);
        if (!level.getFluidState(pos).is(fluidTag) && !Util.getCauldronFluid(level.getBlockState(pos)).is(fluidTag)) {
            remove();
            return;
        }

        if (age++ >= lifetime) {
            remove();
            return;
        }

        xScale += 0.01333F;
        setSpriteFromAge();
    }

    @Override
    public void render(PoseStack poseStack, VertexConsumer consumer, MultiBufferSource.BufferSource bufferSource, Camera camera, float partialTicks) {
        int lightColor = getLightColor(partialTicks);

        poseStack.translate(0, -(6 * (yScale / 4)), 0); // 6 is 1/4 of the model's height
        poseStack.scale(xScale, yScale, xScale);

        model.renderToBuffer(poseStack, bufferSource.getBuffer(model.renderType(texture)), lightColor, OverlayTexture.NO_OVERLAY, alpha, rCol, gCol, bCol);

        if (hasOverlay) {
            model.renderToBuffer(poseStack, bufferSource.getBuffer(model.renderType(overlayTexture)), lightColor, OverlayTexture.NO_OVERLAY, ENTITIES.splashes.splashOverlayAlpha.get(), overlayRCol, overlayGCol, overlayBCol);
        }

        bufferSource.endBatch();
    }

    private void setSpriteFromAge() {
        texture = getSpriteId(sprites.get(age, lifetime));

        if (hasOverlay) {
            overlayTexture = getSpriteId(overlaySprites.get(age, lifetime));
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SplashParticleOptions> {

        @Override
        public Particle createParticle(SplashParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SplashParticle particle = new SplashParticle(level, x, y, z, false, FluidTags.WATER, sprites, WATER_SPLASH_OVERLAY, options);
            int waterColor = level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor();
            float colorIntensity = ENTITIES.splashes.splashOverlayTint.get();
            float red = (waterColor >> 16 & 255) / 255F;
            float green = (waterColor >> 8 & 255) / 255F;
            float blue = (waterColor & 255) / 255F;

            if (colorIntensity > 0) {
                float whiteIntensity = 1 - colorIntensity;

                particle.overlayRCol = whiteIntensity + (colorIntensity * red);
                particle.overlayGCol = whiteIntensity + (colorIntensity * green);
                particle.overlayBCol = whiteIntensity + (colorIntensity * blue);
            }

            particle.setColor(red, green, blue);
            return particle;
        }
    }

    public record LavaProvider(SpriteSet sprites) implements ParticleProvider<SplashParticleOptions> {

        @Override
        public Particle createParticle(SplashParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SplashParticle(level, x, y, z, true, FluidTags.LAVA, sprites, null, options);
        }
    }
}
