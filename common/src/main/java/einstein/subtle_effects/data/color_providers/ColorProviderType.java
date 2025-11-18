package einstein.subtle_effects.data.color_providers;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import einstein.subtle_effects.data.SparkProviderData;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record ColorProviderType<T extends ColorProviderType.ColorProvider>(String name,
                                                                           Supplier<MapCodec<T>> codec) implements StringRepresentable {

    public static final Map<String, ColorProviderType<?>> TYPES = new HashMap<>();

    public static final ColorProviderType<NoneColorProvider> NONE = register("none", () -> NoneColorProvider.CODEC);
    public static final ColorProviderType<ConstantColorProvider> CONSTANT = register("constant", () -> ConstantColorProvider.CODEC);
    public static final ColorProviderType<BiomeWaterColorProvider> BIOME_WATER = register("biome_water", () -> BiomeWaterColorProvider.CODEC);
    public static final ColorProviderType<ListColorProvider> LIST = register("list", () -> ListColorProvider.CODEC);

    @SuppressWarnings("unchecked")
    public static final Codec<ColorProviderType<ColorProvider>> REGISTRY_CODEC = StringRepresentable.fromValues(() -> ColorProviderType.TYPES.values().toArray(new ColorProviderType[0]));
    @SuppressWarnings("unchecked")
    private static final Codec<Either<Integer, ColorProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either(SparkProviderData.Options.RGB_COLOR_CODEC, REGISTRY_CODEC.dispatch(colorProvider -> (ColorProviderType<ColorProvider>) colorProvider.getType(), type -> type.codec().get()));
    public static final Codec<ColorProvider> CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(either -> either.map(ConstantColorProvider::new, colorProvider -> colorProvider), colorProvider -> colorProvider.getType() == NONE ? Either.left(1) : Either.right(colorProvider));

    private static <V extends ColorProvider> ColorProviderType<V> register(String name, Supplier<MapCodec<V>> codec) {
        ColorProviderType<V> type = new ColorProviderType<>(name, codec);
        TYPES.put(name, type);
        return type;
    }

    @Override
    public String getSerializedName() {
        return name();
    }

    public static abstract class ColorProvider {

        public abstract ColorProviderType<?> getType();

        public abstract Vector3f provideColor(Level level, BlockPos pos, RandomSource random);
    }
}
