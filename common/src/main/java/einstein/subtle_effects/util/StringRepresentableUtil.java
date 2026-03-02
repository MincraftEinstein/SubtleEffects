package einstein.subtle_effects.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.Util;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

// Straight backport of the vanilla StringRepresentable.fromValues method from 1.21.1 and all other necessary utilities
public class StringRepresentableUtil {

    public static <T extends StringRepresentable> Codec<T> fromValues(Supplier<T[]> valuesSupplier) {
        T[] at = valuesSupplier.get();
        Function<String, T> function = createNameLookup(at, (string) -> string);
        ToIntFunction<T> tointfunction = Util.createIndexLookup(Arrays.asList(at));
        return new StringRepresentableCodec<T>(at, function, tointfunction);
    }

    static <T extends StringRepresentable> Function<String, T> createNameLookup(T[] values, Function<String, String> keyFunction) {
        if (values.length > 16) {
            Map<String, T> map = Arrays.stream(values).collect(Collectors.toMap((t) -> keyFunction.apply(t.getSerializedName()), (t) -> t));
            return (string) -> string == null ? null : map.get(string);
        }
        else {
            return (string) -> {
                for (T t : values) {
                    if ((keyFunction.apply(t.getSerializedName())).equals(string)) {
                        return t;
                    }
                }

                return null;
            };
        }
    }

    public static class StringRepresentableCodec<S extends StringRepresentable> implements Codec<S> {

        private final Codec<S> codec;

        public StringRepresentableCodec(S[] values, Function<String, S> nameLookup, ToIntFunction<S> indexLookup) {
            codec = ExtraCodecs.orCompressed(stringResolver(StringRepresentable::getSerializedName, nameLookup), ExtraCodecs.idResolverCodec(indexLookup, (p_304986_) -> p_304986_ >= 0 && p_304986_ < values.length ? values[p_304986_] : null, -1));
        }

        public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T value) {
            return codec.decode(ops, value);
        }

        public <T> DataResult<T> encode(S input, DynamicOps<T> ops, T prefix) {
            return codec.encode(input, ops, prefix);
        }
    }

    static <E> Codec<E> stringResolver(Function<E, String> toString, Function<String, E> fromString) {
        return Codec.STRING.flatXmap((name) -> Optional.ofNullable(fromString.apply(name)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown element name:" + name)), (e) -> Optional.ofNullable(toString.apply(e)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Element with unknown name: " + e)));
    }
}
