package einstein.subtle_effects.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

public class CodecUtil {

    public static final Codec<Integer> RGB_COLOR_CODEC = Codec.either(Codec.either(Codec.INT, ExtraCodecs.VECTOR3F).xmap(either ->
            either.map(
                    i -> i,
                    color -> FastColor.ARGB32.color(255, Mth.floor(color.x() * 255), Mth.floor(color.y() * 255), Mth.floor(color.z() * 255))
            ), Either::left
    ), Codec.STRING).comapFlatMap(either -> either.map(DataResult::success, string -> {
        try {
            return DataResult.success(Integer.decode(string));
        }
        catch (NumberFormatException e) {
            return DataResult.error(() -> "String '" + string + "' is not a valid integer color");
        }
    }), Either::left);
    public static final Codec<SimpleParticleType> SIMPLE_PARTICLE_TYPE_CODEC = BuiltInRegistries.PARTICLE_TYPE.byNameCodec().comapFlatMap(options -> {
        if (options instanceof SimpleParticleType particle) {
            return DataResult.success(particle);
        }
        return DataResult.error(() -> "Particle type is not a simple particle type: " + options);
    }, particle -> particle);

    public static Codec<Either<Float, Boolean>> configurableFloatCodec(String floatName) {
        return Codec.either(
                RecordCodecBuilder.create(instance -> instance.group(
                        Codec.floatRange(0, 1).fieldOf(floatName).forGetter(Float::floatValue)
                ).apply(instance, Float::floatValue)),
                RecordCodecBuilder.create(instance -> instance.group(
                        Codec.BOOL.fieldOf("use_config").forGetter(Boolean::booleanValue)
                ).apply(instance, Boolean::booleanValue))
        );
    }
}
