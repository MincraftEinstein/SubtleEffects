package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.init.ModRenderStateAttachmentKeys;
import einstein.subtle_effects.util.RenderStateAttachmentAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.init.ModConfigs.ENVIRONMENT;

@Mixin(WeatherEffectRenderer.class)
public abstract class WeatherEffectsRendererMixin {

    @WrapOperation(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;RAIN_LOCATION:Lnet/minecraft/resources/Identifier;"))
    private Identifier replaceRainTexture(Operation<Identifier> original) {
        if (ENVIRONMENT.biomeColorRain) {
            return Util.COLORLESS_RAIN_TEXTURE;
        }
        return original.call();
    }


    @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;createRainColumnInstance(Lnet/minecraft/util/RandomSource;JIIIIIF)Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;"))
    private WeatherEffectRenderer.ColumnInstance applyColorToColumn(WeatherEffectRenderer instance, RandomSource random, long ticks, int x, int bottomY, int topY, int z, int lightCoords, float partialTicks, Operation<WeatherEffectRenderer.ColumnInstance> original, ClientLevel level,  @Local BlockPos.MutableBlockPos pos) {
        WeatherEffectRenderer.ColumnInstance column = original.call(instance, random, ticks, x, bottomY, topY, z, lightCoords, partialTicks);

        if (ENVIRONMENT.biomeColorRain) {
            BlockPos immutablePos = pos.immutable();
            ((RenderStateAttachmentAccessor) (Object) column).subtleEffects$set(ModRenderStateAttachmentKeys.COLOR, ARGB.vector3fFromRGB24(level.getBiome(immutablePos).value().getWaterColor()));
        }
        return column;
    }

    @WrapOperation(method = "renderInstances", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer replaceWeatherColor(VertexConsumer consumer, int color, Operation<VertexConsumer> original, @Local WeatherEffectRenderer.ColumnInstance column, @Local(ordinal = 6) float alpha) {
        Vector3f waterColor = ((RenderStateAttachmentAccessor) (Object) column).subtleEffects$get(ModRenderStateAttachmentKeys.COLOR);
        if (waterColor != null) {
            return consumer.setColor(waterColor.x(), waterColor.y(), waterColor.z(), alpha);
        }
        return original.call(consumer, color);
    }
}
