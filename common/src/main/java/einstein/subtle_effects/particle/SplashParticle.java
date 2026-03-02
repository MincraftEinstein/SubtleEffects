package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import einstein.subtle_effects.client.model.particle.SplashParticleModel;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.data.FluidDefinitionReloadListener;
import einstein.subtle_effects.data.splash_types.SplashOptions;
import einstein.subtle_effects.data.splash_types.SplashType;
import einstein.subtle_effects.particle.group.ModelParticleGroup;
import einstein.subtle_effects.particle.option.SplashParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.function.Function;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class SplashParticle extends ModelParticle<SplashParticleModel> {

    private final int lightLevel;
    private final FluidDefinition fluidDefinition;
    private final SpriteSet sprites;
    @Nullable
    private final SpriteSet overlaySprites;
    private final boolean hasOverlay;
    private final BlockPos.MutableBlockPos pos;
    private float horizontalScale;
    private final float verticalScale;
    private Identifier texture;
    private Identifier overlayTexture;
    private float overlayRCol = 1;
    private float overlayGCol = 1;
    private float overlayBCol = 1;
    private final float overlayAlpha;
    private final SplashParticleModel model;

    protected SplashParticle(ClientLevel level, double x, double y, double z, SplashParticleOptions options) {
        super(level, x, y, z);
        model = bakeModel(SplashParticleModel::new, SplashParticleModel.MODEL_LAYER);
        fluidDefinition = FluidDefinitionReloadListener.DEFINITIONS.get(options.fluidDefinitionId());
        SplashType type = fluidDefinition.splashType().orElseThrow();
        SplashOptions splashOptions = type.splashOptions();
        SplashOptions overlayOptions = type.splashOverlayOptions();
        float overlayAlpha = alpha(overlayOptions);

        sprites = splashOptions.holder().get();

        // noinspection ConstantConditions
        overlaySprites = overlayOptions.holder() != null ? overlayOptions.holder().get() : null;

        // noinspection ConstantConditions
        hasOverlay = overlaySprites != null && overlayAlpha > 0;
        this.overlayAlpha = overlayAlpha;
        alpha = alpha(splashOptions);
        pos = BlockPos.containing(x, y, z).mutable();
        lifetime = 15;
        lightLevel = fluidDefinition.lightEmission();
        horizontalScale = options.horizontalScale();
        verticalScale = options.verticalScale();
        setSize(horizontalScale, verticalScale);
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

    public static float alpha(SplashOptions options) {
        return alpha(options.transparency());
    }

    public static float alpha(Optional<Either<Float, Boolean>> options) {
        return options.map(transparency ->
                transparency.mapRight(useConfig -> ENTITIES.splashes.splashOverlayAlpha.get())
                        .map(Function.identity(), Function.identity())
        ).orElse(1F);
    }

    public static boolean canNotSurvive(FluidDefinition fluidDefinition, Level level, BlockPos pos) {
        return !fluidDefinition.is(level.getFluidState(pos)) && !fluidDefinition.is(level.getBlockState(pos));
    }

    @Override
    protected int getLightColor(float partialTick) {
        return Math.max(LightTexture.block(lightLevel), super.getLightColor(partialTick));
    }

    @Override
    public void tick() {
        pos.set(x, y, z);
        if (canNotSurvive(fluidDefinition, level, pos)) {
            remove();
            return;
        }

        if (age++ >= lifetime) {
            remove();
            return;
        }

        horizontalScale += 0.01333F;
        setSpriteFromAge();
    }

    @Override
    public ModelParticleGroup.ModelParticleRenderState extractState(PoseStack poseStack, Camera camera, float partialTicks) {
        int lightColor = getLightColor(partialTicks);
        int color = ARGB.colorFromFloat(alpha, rCol, gCol, bCol);

        poseStack.translate(0, -(6 * (verticalScale / 4)), 0); // 6 is 1/4 of the model's height
        poseStack.scale(horizontalScale, verticalScale, horizontalScale);

        ModelParticleGroup.OverlayModelState overlayState = null;
        if (hasOverlay) {
            overlayState = new ModelParticleGroup.OverlayModelState(ARGB.colorFromFloat(overlayAlpha, overlayRCol, overlayGCol, overlayBCol), model.renderType(overlayTexture));
        }

        return new ModelParticleGroup.ModelParticleRenderState(getModel(), poseStack, model.renderType(texture), lightColor, color, overlayState);
    }

    @Override
    public SplashParticleModel getModel() {
        return model;
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
        public Particle createParticle(SplashParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new SplashParticle(level, x, y, z, options);
        }
    }
}
