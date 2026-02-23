package einstein.subtle_effects.data.color_providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
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
        return Vec3.fromRGB24(colorType.colorGetter.apply(level, pos)).toVector3f();
    }

    public enum ColorType implements StringRepresentable {
        WATER_COLOR("water_color", BiomeColors::getAverageWaterColor),
        WATER_FOG_COLOR("water_fog_color", (level, pos) -> level.getBiome(pos).value().getWaterFogColor()),
        SKY_COLOR("sky_color", (level, pos) -> level.getBiome(pos).value().getSkyColor()),
        FOG_COLOR("fog_color", (level, pos) -> level.getBiome(pos).value().getFogColor()),
        FOLIAGE_COLOR("foliage_color", BiomeColors::getAverageFoliageColor),
        GRASS_COLOR("grass_color", BiomeColors::getAverageGrassColor);

        public static final Codec<ColorType> CODEC = StringRepresentable.fromEnum(ColorType::values);

        private final String name;
        private final BiFunction<Level, BlockPos, Integer> colorGetter;

        ColorType(String name, BiFunction<Level, BlockPos, Integer> colorGetter) {
            this.name = name;
            this.colorGetter = colorGetter;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
