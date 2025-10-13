package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.particle.option.SplashParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;
import static einstein.subtle_effects.init.ModSpriteSets.*;
import static net.minecraft.util.Mth.DEG_TO_RAD;

public class SplashParticle extends FlatPlaneParticle {

    private final boolean translucent;
    private final boolean glowing;
    private final TagKey<Fluid> fluidTag;
    private final SpriteSet mainSprites;
    @Nullable
    private final SpriteSet overlaySprites;
    private final SpriteSet bottomSprites;
    private final boolean hasRipple;
    private final boolean hasOverlay;
    private final BlockPos.MutableBlockPos pos;
    private float xScale;
    private final float yScale;
    private TextureAtlasSprite overlaySprite;
    private TextureAtlasSprite bottomSprite;
    private boolean isRipplePhase = false;
    private float overlayRCol = 1;
    private float overlayGCol = 1;
    private float overlayBCol = 1;

    protected SplashParticle(ClientLevel level, double x, double y, double z, boolean translucent, boolean glowing, TagKey<Fluid> fluidTag, SpriteSet mainSprites, @Nullable SpriteSet overlaySprites, SpriteSet bottomSprites, SplashParticleOptions options) {
        super(level, x, y, z, mainSprites.first());
        this.translucent = translucent;
        this.glowing = glowing;
        this.fluidTag = fluidTag;
        this.mainSprites = mainSprites;
        this.overlaySprites = overlaySprites;
        this.bottomSprites = bottomSprites;
        pos = BlockPos.containing(x, y, z).mutable();
        hasOverlay = overlaySprites != null && ENTITIES.splashes.splashOverlayAlpha.get() > 0;
        lifetime = 15;
        hasRipple = options.hasRipple();
        xScale = options.xScale() / 2; // Divided by 2 because it is used as the distance from the center
        yScale = options.yScale() / 2;
        quadSize = xScale;
        setSize(xScale, yScale);
        setSpriteFromAge();
        setBottomSpriteFromAge();
    }

    @Override
    protected Layer getLayer() {
        if (translucent) {
            return Layer.TRANSLUCENT;
        }
        return Layer.OPAQUE;
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
        if (!level.getFluidState(pos).is(fluidTag)) {
            remove();
            return;
        }

        if (age++ >= lifetime) {
            if (!hasRipple || isRipplePhase) {
                remove();
                return;
            }

            isRipplePhase = true;
            lifetime = 20;
            age = 0;
        }

        xScale += 0.005F;

        if (isRipplePhase) {
            setBottomSpriteFromAge();
            return;
        }
        setSpriteFromAge();
    }

    private void setSpriteFromAge() {
        sprite = mainSprites.get(age, lifetime);

        if (hasOverlay) {
            overlaySprite = overlaySprites.get(age, lifetime);
        }
    }

    private void setBottomSpriteFromAge() {
        bottomSprite = bottomSprites.get(age, lifetime);
    }

    @Override
    protected void renderQuad(VertexConsumer consumer, Camera camera, float partialTicks, Quaternionf rotation) {
        Vec3 cameraPos = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, xo, this.x) - cameraPos.x());
        float y = (float) (Mth.lerp(partialTicks, yo, this.y) - cameraPos.y());
        float z = (float) (Mth.lerp(partialTicks, zo, this.z) - cameraPos.z());
        float overlayAlpha = ENTITIES.splashes.splashOverlayAlpha.get();

        if (hasRipple) {
            renderQuad(consumer, new Quaternionf().rotateX(90 * DEG_TO_RAD), partialTicks, x, y, z, false, bottomSprite.getU0(), bottomSprite.getV0(), bottomSprite.getU1(), bottomSprite.getV1(), xScale, xScale, overlayRCol, overlayGCol, overlayBCol, overlayAlpha);
        }

        if (!isRipplePhase) {
            renderBox(consumer, partialTicks, x, y, z, getU0(), getV0(), getU1(), getV1(), rCol, gCol, bCol, alpha);

            if (hasOverlay) {
                renderBox(consumer, partialTicks, x, y, z, overlaySprite.getU0(), overlaySprite.getV0(), overlaySprite.getU1(), overlaySprite.getV1(), overlayRCol, overlayGCol, overlayBCol, overlayAlpha);
            }
        }
    }

    private void renderBox(VertexConsumer consumer, float partialTicks, float x, float y, float z, float u0, float v0, float u1, float v1, float red, float green, float blue, float alpha) {
        renderDoubleQuads(consumer, new Quaternionf(), partialTicks, x, y + yScale, z + xScale, u0, v0, u1, v1, xScale, yScale, red, green, blue, alpha);
        renderDoubleQuads(consumer, new Quaternionf().rotateY(90 * DEG_TO_RAD), partialTicks, x - xScale, y + yScale, z, u0, v0, u1, v1, xScale, yScale, red, green, blue, alpha);
        renderDoubleQuads(consumer, new Quaternionf().rotateY(180 * DEG_TO_RAD), partialTicks, x, y + yScale, z - xScale, u0, v0, u1, v1, xScale, yScale, red, green, blue, alpha);
        renderDoubleQuads(consumer, new Quaternionf().rotateY(-90 * DEG_TO_RAD), partialTicks, x + xScale, y + yScale, z, u0, v0, u1, v1, xScale, yScale, red, green, blue, alpha);
    }

    protected void renderDoubleQuads(VertexConsumer consumer, Quaternionf rotation, float partialTicks, float x, float y, float z, float u0, float v0, float u1, float v1, float quadWidthSize, float quadHeightSize, float red, float green, float blue, float alpha) {
        renderQuad(consumer, rotation, partialTicks, x, y, z, false, u0, v0, u1, v1, quadWidthSize, quadHeightSize, red, green, blue, alpha);
        renderQuad(consumer, rotation, partialTicks, x, y, z, true, u0, v0, u1, v1, quadWidthSize, quadHeightSize, red, green, blue, alpha);
    }

    protected void renderQuad(VertexConsumer consumer, Quaternionf rotation, float partialTicks, float x, float y, float z, boolean renderInverted, float u0, float v0, float u1, float v1, float quadWidthSize, float quadHeightSize, float red, float green, float blue, float alpha) {
        int packedLight = getLightColor(partialTicks);
        int i = renderInverted ? 1 : -1;
        int i2 = renderInverted ? -1 : 1;

        renderVertex(consumer, rotation, x, y, z, i, -1, quadWidthSize, quadHeightSize, u1, v1, packedLight, red, green, blue, alpha);
        renderVertex(consumer, rotation, x, y, z, i, 1, quadWidthSize, quadHeightSize, u1, v0, packedLight, red, green, blue, alpha);
        renderVertex(consumer, rotation, x, y, z, i2, 1, quadWidthSize, quadHeightSize, u0, v0, packedLight, red, green, blue, alpha);
        renderVertex(consumer, rotation, x, y, z, i2, -1, quadWidthSize, quadHeightSize, u0, v1, packedLight, red, green, blue, alpha);
    }

    protected void renderVertex(VertexConsumer buffer, Quaternionf rotation, float x, float y, float z, float xOffset, float yOffset, float quadWidthSize, float quadHeightSize, float u, float v, int packedLight, float red, float green, float blue, float alpha) {
        Vector3f vector3f = new Vector3f(xOffset, yOffset, 0).rotate(rotation).mul(quadWidthSize, quadHeightSize, quadWidthSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(red, green, blue, alpha).setLight(packedLight);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SplashParticleOptions> {

        @Override
        public Particle createParticle(SplashParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            SplashParticle particle = new SplashParticle(level, x, y, z, true, false, FluidTags.WATER, sprites, WATER_SPLASH_OVERLAY, WATER_SPLASH_BOTTOM, options);
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
        public Particle createParticle(SplashParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new SplashParticle(level, x, y, z, false, true, FluidTags.LAVA, sprites, null, LAVA_SPLASH_BOTTOM, options);
        }
    }
}
