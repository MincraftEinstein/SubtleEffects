package einstein.subtle_effects.data.splash_types;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record SplashType(int lightEmission, SplashOptionsData.SplashOptions splashOptions,
                         SplashOptionsData.SplashOptions splashOverlayOptions,
                         SplashOptionsData.SplashOptions splashRippleOptions,
                         SplashOptionsData.SplashOptions dropletOptions) {

    public record Data(int lightEmission, SplashOptionsData splashOptions,
                       Either<Boolean, SplashOptionsData> splashOverlayOptions,
                       SplashOptionsData splashRippleOptions, Optional<SplashOptionsData> dropletOptions) {

        public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.intRange(0, 15).optionalFieldOf("light_emission", 0).forGetter(Data::lightEmission),
                SplashOptionsData.CODEC.optionalFieldOf("splash", SplashOptionsData.EMPTY).forGetter(Data::splashOptions),
                Codec.either(Codec.BOOL, SplashOptionsData.CODEC).optionalFieldOf("splash_overlay", Either.left(false)).forGetter(Data::splashOverlayOptions),
                SplashOptionsData.CODEC.optionalFieldOf("splash_ripple", SplashOptionsData.EMPTY).forGetter(Data::splashRippleOptions),
                SplashOptionsData.DROPLETS_CODEC.optionalFieldOf("droplets").forGetter(Data::dropletOptions)
        ).apply(instance, Data::new));
    }
}
