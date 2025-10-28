package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import einstein.subtle_effects.util.Util;
import einstein.subtle_effects.util.WeatherColumnInstance;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static einstein.subtle_effects.init.ModConfigs.ENVIRONMENT;
import static einstein.subtle_effects.util.MathUtil.nextDouble;

@Mixin(WeatherEffectRenderer.class)
public abstract class WeatherEffectsRendererMixin {

    @Shadow
    protected abstract Biome.Precipitation getPrecipitationAt(Level p_362885_, BlockPos p_362817_);

    @WrapOperation(method = "render*", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;RAIN_LOCATION:Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation replaceRainTexture(Operation<ResourceLocation> original) {
        if (ENVIRONMENT.biomeColorRain) {
            return Util.COLORLESS_RAIN_TEXTURE;
        }
        return original.call();
    }

    @ModifyExpressionValue(method = "tickRainParticles", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;SMOKE:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceRainEvaporationParticle(SimpleParticleType original) {
        if (BLOCKS.steam.replaceRainEvaporationSteam) {
            return ModParticles.STEAM.get();
        }
        return original;
    }

    // 'original' does not capture the '!', so the returned expression must be written inverted
    @ModifyExpressionValue(method = "tickRainParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean modifyRainEvaporationBlocks(boolean original, @Local BlockState state) {
        return original || (BLOCKS.steam.lavaCauldronsEvaporateRain && state.is(Blocks.LAVA_CAULDRON));
    }

    @WrapOperation(method = "tickRainParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void modifyCauldronRippleParticlePos(ClientLevel level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original, @Local BlockState state, @Local FluidState fluidState, @Local RandomSource random, @Local(ordinal = 1) BlockPos pos) {
        if (BLOCKS.rainWaterRipples) {
            boolean isCauldron = state.is(Blocks.WATER_CAULDRON);

            if ((fluidState.is(FluidTags.WATER) || isCauldron)) {
                if (random.nextDouble() > BLOCKS.rainWaterRipplesDensity.get()) {
                    return;
                }

                options = new FloatParticleOptions(ModParticles.WATER_RIPPLE.get(), 1);

                if (isCauldron) {
                    x = pos.getX() + 0.1875 + nextDouble(random, 0.625);
                    y = pos.getY() + Util.getCauldronFillHeight(state) + 0.01;
                    z = pos.getZ() + 0.1875 + nextDouble(random, 0.625);
                }
            }
        }
        original.call(level, options, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;createRainColumnInstance(Lnet/minecraft/util/RandomSource;IIIIIIF)Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;"))
    private WeatherEffectRenderer.ColumnInstance collectColumnInstances(WeatherEffectRenderer instance, RandomSource p_364494_, int p_361188_, int p_362466_, int p_364844_, int p_361656_, int p_364160_, int p_361622_, float p_363800_, Operation<WeatherEffectRenderer.ColumnInstance> original, Level level, @Local BlockPos.MutableBlockPos pos) {
        WeatherEffectRenderer.ColumnInstance column = original.call(instance, p_364494_, p_361188_, p_362466_, p_364844_, p_361656_, p_364160_, p_361622_, p_363800_);
        ((WeatherColumnInstance) (Object) column).subtleEffects$set(level, pos.immutable());
        return column;
    }

    @WrapOperation(method = "renderInstances", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer renderSnowAndRain(VertexConsumer instance, int color, Operation<VertexConsumer> original, @Local WeatherEffectRenderer.ColumnInstance column, @Local(ordinal = 4) float alpha) {
        WeatherColumnInstance weatherColumn = (WeatherColumnInstance) (Object) column;
        Level level = weatherColumn.subtleEffects$getLevel();

        if (level != null) {
            BlockPos pos = weatherColumn.subtleEffects$getPos();

            if (getPrecipitationAt(level, pos) == Biome.Precipitation.RAIN && ENVIRONMENT.biomeColorRain) {
                int waterColor = level.getBiome(pos).value().getWaterColor();
                return instance.setColor((waterColor >> 16) / 255F, (waterColor >> 8) / 255F, waterColor / 255F, alpha);
            }
        }
        return original.call(instance, color);
    }
}
