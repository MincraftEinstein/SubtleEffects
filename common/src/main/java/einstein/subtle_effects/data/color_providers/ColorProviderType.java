package einstein.subtle_effects.data.color_providers;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.StringRepresentableUtil;
import einstein.subtle_effects.util.CodecUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public record ColorProviderType<T extends ColorProviderType.ColorProvider>(ResourceLocation registryName,
                                                                           Supplier<MapCodec<T>> codec,
                                                                           Function<FriendlyByteBuf, ColorProvider> reader) implements StringRepresentable {

    public static final Map<ResourceLocation, ColorProviderType<?>> TYPES = new HashMap<>();

    public static final ColorProviderType<NoneColorProvider> NONE = register("none", () -> NoneColorProvider.CODEC, buf -> NoneColorProvider.INSTANCE);
    public static final ColorProviderType<ConstantColorProvider> CONSTANT = register("constant", () -> ConstantColorProvider.CODEC, ConstantColorProvider::read);
    public static final ColorProviderType<BiomeColorProvider> BIOME_COLOR = register("biome_color", () -> BiomeColorProvider.CODEC, BiomeColorProvider::read);
    public static final ColorProviderType<ListColorProvider> LIST = register("list", () -> ListColorProvider.CODEC, ListColorProvider::read);
    public static final ColorProviderType<PresetColorProvider> PRESET = register("preset", () -> PresetColorProvider.CODEC, PresetColorProvider::read);

    public static final Codec<ColorProviderType<?>> REGISTRY_CODEC = StringRepresentableUtil.fromValues(() -> ColorProviderType.TYPES.values().toArray(new ColorProviderType<?>[0]));
    private static final Codec<Either<Integer, ColorProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either(CodecUtil.RGB_COLOR_CODEC, REGISTRY_CODEC.dispatch(ColorProvider::getType, type -> type.codec().get().codec()));
    public static final Codec<ColorProvider> CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(
            either -> either.map(ConstantColorProvider::new, colorProvider -> colorProvider),
            colorProvider -> colorProvider.getType() == NONE ? Either.left(1) : Either.right(colorProvider)
    );

    public static void init() {
    }

    private static <V extends ColorProvider> ColorProviderType<V> register(String name, Supplier<MapCodec<V>> codec, Function<FriendlyByteBuf, ColorProvider> reader) {
        ResourceLocation registryName = SubtleEffects.loc(name);
        ColorProviderType<V> type = new ColorProviderType<>(registryName, codec, reader);
        if (TYPES.put(registryName, type) != null) {
            throw new IllegalStateException("Duplicate color provider type: " + registryName);
        }
        return type;
    }

    public static ColorProvider read(FriendlyByteBuf buf) {
        return TYPES.get(buf.readResourceLocation()).reader.apply(buf);
    }

    public static void write(FriendlyByteBuf buf, ColorProvider provider) {
        buf.writeResourceLocation(provider.getType().registryName());
        provider.write(buf);
    }

    @Override
    public String getSerializedName() {
        return registryName().toString();
    }

    public interface ColorProvider {

        ColorProviderType<?> getType();

        Vector3f provideColor(Level level, BlockPos pos, RandomSource random);

        default Vector3f provideColor(Level level, double x, double y, double z, RandomSource random) {
            return provideColor(level, BlockPos.containing(x, y, z), random);
        }

        void write(FriendlyByteBuf buf);
    }
}
