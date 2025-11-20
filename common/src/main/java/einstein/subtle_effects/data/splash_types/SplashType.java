package einstein.subtle_effects.data.splash_types;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.DropletOptions;

import java.util.Optional;

public record SplashType(SplashOptionsData.SplashOptions splashOptions,
                         SplashOptionsData.SplashOptions splashOverlayOptions,
                         SplashOptionsData.SplashOptions splashRippleOptions,
                         DropletOptions dropletOptions) {

    public record Data(SplashOptionsData splashOptions,
                       Either<Boolean, SplashOptionsData> splashOverlayOptions,
                       SplashOptionsData splashRippleOptions, Optional<DropletOptions> dropletOptions) {

        public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SplashOptionsData.CODEC.optionalFieldOf("splash", SplashOptionsData.EMPTY).forGetter(Data::splashOptions),
                Codec.either(Codec.BOOL, SplashOptionsData.CODEC).optionalFieldOf("splash_overlay", Either.left(false)).forGetter(Data::splashOverlayOptions),
                SplashOptionsData.CODEC.optionalFieldOf("splash_ripple", SplashOptionsData.EMPTY).forGetter(Data::splashRippleOptions),
                DropletOptions.CODEC.optionalFieldOf("droplets").forGetter(Data::dropletOptions)
        ).apply(instance, Data::new));
    }
}
