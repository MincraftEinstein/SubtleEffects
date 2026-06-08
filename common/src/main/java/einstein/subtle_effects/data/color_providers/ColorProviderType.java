package einstein.subtle_effects.data.color_providers;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record ColorProviderType<T extends ColorProviderType.ColorProvider>(ResourceLocation registryName,
                                                                           Supplier<MapCodec<T>> codec,
                                                                           Supplier<StreamCodec<ByteBuf, T>> streamCodec) implements StringRepresentable {

    public static final Map<ResourceLocation, ColorProviderType<?>> TYPES = new HashMap<>();

    public static final ColorProviderType<NoneColorProvider> NONE = register("none", () -> NoneColorProvider.CODEC, () -> NoneColorProvider.STREAM_CODEC);
    public static final ColorProviderType<ConstantColorProvider> CONSTANT = register("constant", () -> ConstantColorProvider.CODEC, () -> ConstantColorProvider.STREAM_CODEC);
    public static final ColorProviderType<BiomeColorProvider> BIOME_COLOR = register("biome_color", () -> BiomeColorProvider.CODEC, () -> BiomeColorProvider.STREAM_CODEC);
    public static final ColorProviderType<ListColorProvider> LIST = register("list", () -> ListColorProvider.CODEC, () -> ListColorProvider.STREAM_CODEC);
    public static final ColorProviderType<PresetColorProvider> PRESET = register("preset", () -> PresetColorProvider.CODEC, () -> PresetColorProvider.STREAM_CODEC);

    public static final Codec<ColorProviderType<?>> REGISTRY_CODEC = StringRepresentable.fromValues(() -> ColorProviderType.TYPES.values().toArray(new ColorProviderType<?>[0]));
    public static final StreamCodec<ByteBuf, ColorProviderType<?>> REGISTRY_STREAM_CODEC = StreamCodec.composite(ResourceLocation.STREAM_CODEC, ColorProviderType::registryName, TYPES::get);
    private static final Codec<Either<Integer, ColorProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either(Util.RGB_COLOR_CODEC, REGISTRY_CODEC.dispatch(ColorProvider::getType, type -> type.codec().get()));
    public static final Codec<ColorProvider> CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(
            either -> either.map(ConstantColorProvider::new, colorProvider -> colorProvider),
            colorProvider -> colorProvider.getType() == NONE ? Either.left(1) : Either.right(colorProvider)
    );
    public static final StreamCodec<ByteBuf, ColorProvider> STREAM_CODEC = REGISTRY_STREAM_CODEC.dispatch(ColorProvider::getType, type -> type.streamCodec().get());

    public static void init() {
    }

    private static <V extends ColorProvider> ColorProviderType<V> register(String name, Supplier<MapCodec<V>> codec, Supplier<StreamCodec<ByteBuf, V>> streamCodec) {
        ResourceLocation registryName = SubtleEffects.loc(name);
        ColorProviderType<V> type = new ColorProviderType<>(registryName, codec, streamCodec);
        if (TYPES.put(registryName, type) != null) {
            throw new IllegalStateException("Duplicate color provider type: " + registryName);
        }
        return type;
    }

    @Override
    public String getSerializedName() {
        return registryName().toString();
    }

    public interface ColorProvider {

        ColorProviderType<?> getType();

        Vector3f provideColor(Level level, BlockPos pos, RandomSource random);
    }
}
