package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Either;
import einstein.subtle_effects.client.model.particle.SplashParticleModel;
import einstein.subtle_effects.data.FluidPair;
import einstein.subtle_effects.data.FluidPairReloadListener;
import einstein.subtle_effects.data.splash_types.SplashOptionsData;
import einstein.subtle_effects.data.splash_types.SplashType;
import einstein.subtle_effects.particle.option.SplashParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.function.Function;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class SplashParticle extends ModelParticle {

    private final int lightLevel;
    private final FluidPair fluidPair;
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
    private final float overlayAlpha;
    private final SplashParticleModel model;

    protected SplashParticle(ClientLevel level, double x, double y, double z, SplashParticleOptions options) {
        super(level, x, y, z);
        model = bakeModel(SplashParticleModel::new, SplashParticleModel.MODEL_LAYER);
        fluidPair = FluidPairReloadListener.FLUID_PAIRS.get(options.fluidPairId());
        SplashType type = fluidPair.splashType().orElseThrow();
        SplashOptionsData.SplashOptions splashOptions = type.splashOptions();
        SplashOptionsData.SplashOptions overlayOptions = type.splashOverlayOptions();
        float overlayAlpha = alpha(overlayOptions);

        sprites = splashOptions.sprites();
        overlaySprites = overlayOptions.sprites();
        hasOverlay = overlaySprites != null && overlayAlpha > 0;
        this.overlayAlpha = overlayAlpha;
        alpha = alpha(splashOptions);
        pos = BlockPos.containing(x, y, z).mutable();
        lifetime = 15;
        lightLevel = fluidPair.lightEmission();
        xScale = options.xScale();
        yScale = options.yScale();
        setSize(xScale, yScale);
        setSpriteFromAge();

        Vector3f color = splashOptions.getColorAndApplyTint(level, pos, random);
        rCol = color.x;
        gCol = color.y;
        bCol = color.z;

        if (hasOverlay) {
            Vector3f overlayColor = overlayOptions.getColorAndApplyTint(level, pos, random);
            overlayRCol = overlayColor.x;
            overlayGCol = overlayColor.y;
            overlayBCol = overlayColor.z;
        }
    }

    public static float alpha(SplashOptionsData.SplashOptions options) {
        return alpha(options.transparency());
    }

    public static float alpha(Optional<Either<Float, Boolean>> options) {
        return options.map(transparency ->
                transparency.mapRight(useConfig -> ENTITIES.splashes.splashOverlayAlpha.get())
                        .map(Function.identity(), Function.identity())
        ).orElse(1F);
    }

    public static boolean canNotSurvive(FluidPair fluidPair, Level level, BlockPos pos) {
        return !fluidPair.is(level.getFluidState(pos)) && !fluidPair.is(level.getBlockState(pos));
    }

    @Override
    protected int getLightColor(float partialTick) {
        return Math.max(LightTexture.block(lightLevel), super.getLightColor(partialTick));
    }

    @Override
    public void tick() {
        pos.set(x, y, z);
        if (canNotSurvive(fluidPair, level, pos)) {
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
        int color = FastColor.ARGB32.colorFromFloat(alpha, rCol, gCol, bCol);

        poseStack.translate(0, -(6 * (yScale / 4)), 0); // 6 is 1/4 of the model's height
        poseStack.scale(xScale, yScale, xScale);

        model.renderToBuffer(poseStack, bufferSource.getBuffer(model.renderType(texture)), lightColor, OverlayTexture.NO_OVERLAY, color);

        if (hasOverlay) {
            int overlayColor = FastColor.ARGB32.colorFromFloat(overlayAlpha, overlayRCol, overlayGCol, overlayBCol);
            model.renderToBuffer(poseStack, bufferSource.getBuffer(model.renderType(overlayTexture)), lightColor, OverlayTexture.NO_OVERLAY, overlayColor);
        }

        bufferSource.endBatch();
    }

    private void setSpriteFromAge() {
        texture = getSpriteId(sprites.get(age, lifetime));

        if (hasOverlay) {
            // noinspection ConstantConditions
            overlayTexture = getSpriteId(overlaySprites.get(age, lifetime));
        }
    }

    public record Provider() implements ParticleProvider<SplashParticleOptions> {

        @Override
        public Particle createParticle(SplashParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SplashParticle(level, x, y, z, options);
        }
    }
}
