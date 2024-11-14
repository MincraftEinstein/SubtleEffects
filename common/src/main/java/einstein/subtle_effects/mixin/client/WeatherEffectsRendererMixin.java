package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.util.Util;
import einstein.subtle_effects.util.WeatherColumnInstance;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.init.ModConfigs.BIOMES;

@Mixin(WeatherEffectRenderer.class)
public abstract class WeatherEffectsRendererMixin {

    @Shadow
    protected abstract Biome.Precipitation getPrecipitationAt(Level p_362885_, BlockPos p_362817_);

    @WrapOperation(method = "render(Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/world/phys/Vec3;IFLjava/util/List;Ljava/util/List;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;RAIN_LOCATION:Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation replaceRainTexture(Operation<ResourceLocation> original) {
        if (BIOMES.biomeColorRain) {
            return Util.COLORLESS_RAIN_TEXTURE;
        }
        return original.call();
    }

    @WrapOperation(method = "collectColumnInstances", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;createRainColumnInstance(Lnet/minecraft/util/RandomSource;IIIIIIF)Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;"))
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

            if (getPrecipitationAt(level, pos) == Biome.Precipitation.RAIN && BIOMES.biomeColorRain) {
                int waterColor = level.getBiome(pos).value().getWaterColor();
                return instance.setColor((waterColor >> 16) / 255F, (waterColor >> 8) / 255F, waterColor / 255F, alpha);
            }
        }
        return original.call(instance, color);
    }
}
