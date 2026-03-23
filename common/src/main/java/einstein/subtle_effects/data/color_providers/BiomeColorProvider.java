package einstein.subtle_effects.data.color_providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.function.BiFunction;

public record BiomeColorProvider(ColorType colorType) implements ColorProviderType.ColorProvider {

    public static final MapCodec<BiomeColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ColorType.CODEC.fieldOf("color_type").forGetter(BiomeColorProvider::colorType)
    ).apply(instance, BiomeColorProvider::new));

    @Override
    public ColorProviderType<?> getType() {
        return ColorProviderType.BIOME_WATER;
    }

    @Override
    public Vector3f provideColor(Level level, BlockPos pos, RandomSource random) {
        return ARGB.vector3fFromRGB24(colorType.colorGetter.apply((ClientLevel) level, pos));
    }

    public enum ColorType implements StringRepresentable {
        WATER_COLOR("water_color", BiomeColors::getAverageWaterColor),
        WATER_FOG_COLOR("water_fog_color", EnvironmentAttributes.WATER_FOG_COLOR),
        SKY_COLOR("sky_color", EnvironmentAttributes.SKY_COLOR),
        FOG_COLOR("fog_color", EnvironmentAttributes.FOG_COLOR),
        SUNRISE_SUNSET_COLOR("sunrise_sunset_color", EnvironmentAttributes.SUNRISE_SUNSET_COLOR),
        CLOUD_COLOR("cloud_color", EnvironmentAttributes.CLOUD_COLOR),
        SKY_LIGHT_COLOR("sky_light_color", EnvironmentAttributes.SKY_LIGHT_COLOR),
        FOLIAGE_COLOR("foliage_color", BiomeColors::getAverageFoliageColor),
        DRY_FOLIAGE_COLOR("dry_foliage_color", BiomeColors::getAverageDryFoliageColor),
        GRASS_COLOR("grass_color", BiomeColors::getAverageGrassColor);

        public static final Codec<ColorType> CODEC = StringRepresentable.fromEnum(ColorType::values);

        private final String name;
        private final BiFunction<BlockAndTintGetter, BlockPos, Integer> colorGetter;

        ColorType(String name, BiFunction<BlockAndTintGetter, BlockPos, Integer> colorGetter) {
            this.name = name;
            this.colorGetter = colorGetter;
        }

        ColorType(String name, EnvironmentAttribute<Integer> attribute) {
            this(name, (level, pos) ->
                    Minecraft.getInstance().gameRenderer.getMainCamera()
                            .attributeProbe().getValue(attribute, Util.getPartialTicks())
            );
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
